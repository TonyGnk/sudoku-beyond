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

package com.kaajjo.libresudoku.ui.folders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaajjo.libresudoku.core.parser.SdmParser
import com.kaajjo.libresudoku.data.database.model.Folder
import com.kaajjo.libresudoku.domain.usecase.board.GetGamesInFolderUseCase
import com.kaajjo.libresudoku.domain.usecase.folder.CountPuzzlesFolderUseCase
import com.kaajjo.libresudoku.domain.usecase.folder.DeleteFolderUseCase
import com.kaajjo.libresudoku.domain.usecase.folder.GetFoldersUseCase
import com.kaajjo.libresudoku.domain.usecase.folder.GetLastSavedGamesAnyFolderUseCase
import com.kaajjo.libresudoku.domain.usecase.folder.InsertFolderUseCase
import com.kaajjo.libresudoku.domain.usecase.folder.UpdateFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class FoldersViewModel @Inject constructor(
    getFoldersUseCase: GetFoldersUseCase,
    private val insertFolderUseCase: InsertFolderUseCase,
    private val updateFolderUseCase: UpdateFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val getGamesInFolderUseCase: GetGamesInFolderUseCase,
    private val countPuzzlesFolderUseCase: CountPuzzlesFolderUseCase,
    getLastSavedGamesAnyFolderUseCase: GetLastSavedGamesAnyFolderUseCase
) : ViewModel() {
    val folders = getFoldersUseCase()

    var selectedFolder: Folder? by mutableStateOf(null)

    private val _sudokuListToImport = MutableStateFlow(emptyList<String>())
    val sudokuListToImport = _sudokuListToImport.asStateFlow()

    var puzzlesCountInFolder by mutableStateOf(emptyList<Pair<Long, Int>>())

    val lastSavedGames = getLastSavedGamesAnyFolderUseCase(gamesCount = 10)

    fun createFolder(name: String) {
        viewModelScope.launch {
            insertFolderUseCase(
                Folder(
                    uid = 0,
                    name = name.trim(),
                    createdAt = ZonedDateTime.now()
                )
            )
        }
    }

    fun countPuzzlesInFolders(folders: List<Folder>) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberPuzzles = mutableListOf<Pair<Long, Int>>()
            folders.forEach {
                numberPuzzles.add(
                    Pair(it.uid, countPuzzlesFolderUseCase(it.uid).toInt())
                )
            }
            puzzlesCountInFolder = numberPuzzles
        }
    }

    fun renameFolder(newName: String) {
        selectedFolder?.let {
            viewModelScope.launch {
                updateFolderUseCase(
                    it.copy(name = newName.trim())
                )
            }
        }
    }

    fun deleteFolder() {
        selectedFolder?.let {
            viewModelScope.launch {
                deleteFolderUseCase(it)
            }
        }
    }

    fun generateFolderExportData(): ByteArray {
        val gamesInFolder = getGamesInFolderUseCase(selectedFolder!!.uid)

        val sdmParser = SdmParser()
        return sdmParser.boardsToSdm(gamesInFolder).toByteArray()
    }
}