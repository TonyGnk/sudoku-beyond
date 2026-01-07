/*
 * Copyright (C) 2022-2025 kaajjo
 * Copyright (C) 2026 TonyGnk
 *
 * This file is part of Sudoku Beyond.
 * Originally from LibreSudoku (https://github.com/kaajjo/LibreSudoku)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package gr.tonygnk.sudokubeyond.ui.backup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudokubeyond.BuildConfig
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.data.backup.BackupData
import gr.tonygnk.sudokubeyond.data.backup.SettingsBackup
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.domain.repository.BoardRepository
import gr.tonygnk.sudokubeyond.domain.repository.DatabaseRepository
import gr.tonygnk.sudokubeyond.domain.repository.FolderRepository
import gr.tonygnk.sudokubeyond.domain.repository.RecordRepository
import gr.tonygnk.sudokubeyond.domain.repository.SavedGameRepository
import gr.tonygnk.sudokubeyond.ui.util.viewModelBuilder
import gr.tonygnk.sudokubeyond.util.FlavorUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.OutputStream
import java.time.ZonedDateTime

class BackupScreenViewModel(
    private val appSettingsManager: AppSettingsManager,
    private val themeSettingsManager: ThemeSettingsManager,
    private val boardRepository: BoardRepository,
    private val folderRepository: FolderRepository,
    private val recordRepository: RecordRepository,
    private val savedGameRepository: SavedGameRepository,
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {
    val backupUri = appSettingsManager.backupUri.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialValue = ""
    )

    var backupData by mutableStateOf<BackupData?>(null)
    private var backupJson: String? = null
    var restoreError by mutableStateOf(false)
    var restoreExceptionString by mutableStateOf("")

    val autoBackupsNumber = appSettingsManager.autoBackupsNumber
    val autoBackupInterval = appSettingsManager.autoBackupInterval
    val lastBackupDate = appSettingsManager.lastBackupDate
    val dateFormat = appSettingsManager.dateFormat

    fun createBackup(
        backupSettings: Boolean,
        onCreated: (Boolean) -> Unit,
    ) {
        try {
            val boards = runBlocking { boardRepository.getAll().first() }
            val folders = runBlocking { folderRepository.getAll().first() }
            val records = runBlocking { recordRepository.getAll().first() }
            val savedGames = runBlocking { savedGameRepository.getAll().first() }

            backupData = BackupData(
                appVersionName = BuildConfig.VERSION_NAME + if (FlavorUtil.isFoss()) "-FOSS" else "",
                appVersionCode = BuildConfig.VERSION_CODE,
                createdAt = ZonedDateTime.now(),
                boards = boards,
                folders = folders,
                records = records,
                savedGames = savedGames,
                settings = if (backupSettings) SettingsBackup.getSettings(
                    appSettingsManager,
                    themeSettingsManager
                ) else null
            )

            val json = Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
            }
            backupJson = json.encodeToString(backupData)
            onCreated(true)
        } catch (e: Exception) {
            onCreated(false)
        }
    }

    fun setBackupDirectory(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appSettingsManager.setBackupUri(uri)
        }
    }

    fun prepareBackupToRestore(
        backupString: String,
        onComplete: () -> Unit,
    ) {
        try {
            val json = Json { ignoreUnknownKeys = true }
            backupData = json.decodeFromString<BackupData?>(backupString)
            onComplete()
        } catch (e: Exception) {
            restoreError = true
            restoreExceptionString = e.message.toString()
        }
    }

    fun saveBackupTo(
        outputStream: OutputStream?,
        onComplete: (Throwable?) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            backupJson?.let { backup ->
                try {
                    outputStream?.use {
                        it.write(backup.toByteArray())
                        it.close()
                    }
                    onComplete(null)
                    viewModelScope.launch(Dispatchers.IO) {
                        appSettingsManager.setLastBackupDate(ZonedDateTime.now())
                    }
                } catch (e: Exception) {
                    onComplete(e)
                }
            }
        }
    }

    fun restoreBackup(
        onComplete: () -> Unit,
    ) {
        backupData?.let { backup ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    // deleting all data from database
                    runBlocking { databaseRepository.resetDb() }

                    if (backup.boards.isNotEmpty()) {
                        folderRepository.insert(backup.folders)
                        boardRepository.insert(backup.boards)
                        savedGameRepository.insert(backup.savedGames)
                        recordRepository.insert(backup.records)
                    }

                    backup.settings?.setSettings(appSettingsManager, themeSettingsManager)
                    onComplete()
                } catch (e: Exception) {
                    restoreError = true
                    restoreExceptionString = e.message.toString()
                }
            }
        }
    }

    fun setAutoBackupsNumber(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            appSettingsManager.setAutoBackupsNumber(value)
        }
    }

    fun setAutoBackupInterval(hours: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            appSettingsManager.setAutoBackupInterval(hours)
        }
    }

    companion object {
        val builder = viewModelBuilder {
            BackupScreenViewModel(
                appSettingsManager = LibreSudokuApp.appModule.appSettingsManager,
                themeSettingsManager = LibreSudokuApp.appModule.themeSettingsManager,
                boardRepository = LibreSudokuApp.appModule.boardRepository,
                folderRepository = LibreSudokuApp.appModule.folderRepository,
                recordRepository = LibreSudokuApp.appModule.recordRepository,
                savedGameRepository = LibreSudokuApp.appModule.savedGameRepository,
                databaseRepository = LibreSudokuApp.appModule.databaseRepository
            )
        }
    }
}