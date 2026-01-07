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

package gr.tonygnk.sudokubeyond.di

import android.app.Application
import gr.tonygnk.sudokubeyond.data.database.AppDatabase
import gr.tonygnk.sudokubeyond.data.database.dao.BoardDao
import gr.tonygnk.sudokubeyond.data.database.dao.FolderDao
import gr.tonygnk.sudokubeyond.data.database.dao.RecordDao
import gr.tonygnk.sudokubeyond.data.database.dao.SavedGameDao
import gr.tonygnk.sudokubeyond.data.database.repository.BoardRepositoryImpl
import gr.tonygnk.sudokubeyond.data.database.repository.DatabaseRepositoryImpl
import gr.tonygnk.sudokubeyond.data.database.repository.FolderRepositoryImpl
import gr.tonygnk.sudokubeyond.data.database.repository.RecordRepositoryImpl
import gr.tonygnk.sudokubeyond.data.database.repository.SavedGameRepositoryImpl
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.TipCardsDataStore
import gr.tonygnk.sudokubeyond.domain.repository.BoardRepository
import gr.tonygnk.sudokubeyond.domain.repository.DatabaseRepository
import gr.tonygnk.sudokubeyond.domain.repository.FolderRepository
import gr.tonygnk.sudokubeyond.domain.repository.RecordRepository
import gr.tonygnk.sudokubeyond.domain.repository.SavedGameRepository

internal interface AppModule {
    val databaseRepository: DatabaseRepository
    val folderRepository: FolderRepository
    val folderDao: FolderDao
    val recordRepository: RecordRepository
    val recordDao: RecordDao
    val boardRepository: BoardRepository
    val boardDao: BoardDao
    val savedGameRepository: SavedGameRepository
    val savedGameDao: SavedGameDao
    val appSettingsManager: AppSettingsManager
    val themeSettingsManager: ThemeSettingsManager
    val tipCardsDataStore: TipCardsDataStore
    val appDatabase: AppDatabase
}

internal class AppModuleImpl(
    private val context: Application,
) : AppModule {
    override val databaseRepository: DatabaseRepository by lazy {
        DatabaseRepositoryImpl(appDatabase)
    }

    override val folderRepository: FolderRepository by lazy {
        FolderRepositoryImpl(folderDao)
    }

    override val folderDao: FolderDao by lazy {
        appDatabase.folderDao()
    }

    override val recordRepository: RecordRepository by lazy {
        RecordRepositoryImpl(recordDao)
    }

    override val recordDao: RecordDao by lazy {
        appDatabase.recordDao()
    }

    override val boardRepository: BoardRepository by lazy {
        BoardRepositoryImpl(boardDao)
    }

    override val boardDao: BoardDao by lazy {
        appDatabase.boardDao()
    }

    override val savedGameRepository: SavedGameRepository by lazy {
        SavedGameRepositoryImpl(savedGameDao)
    }

    override val savedGameDao: SavedGameDao by lazy {
        appDatabase.savedGameDao()
    }

    override val appSettingsManager: AppSettingsManager by lazy {
        AppSettingsManager(context)
    }

    override val themeSettingsManager: ThemeSettingsManager by lazy {
        ThemeSettingsManager(context)
    }

    override val tipCardsDataStore: TipCardsDataStore by lazy {
        TipCardsDataStore(context)
    }

    override val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }
}