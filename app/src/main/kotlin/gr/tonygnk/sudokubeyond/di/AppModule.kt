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
import android.content.Context
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
import gr.tonygnk.sudokubeyond.domain.repository.BoardRepository
import gr.tonygnk.sudokubeyond.domain.repository.DatabaseRepository
import gr.tonygnk.sudokubeyond.domain.repository.FolderRepository
import gr.tonygnk.sudokubeyond.domain.repository.RecordRepository
import gr.tonygnk.sudokubeyond.domain.repository.SavedGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabaseRepository(appDatabase: AppDatabase): DatabaseRepository
        = DatabaseRepositoryImpl(appDatabase)

    @Provides
    @Singleton
    fun provideFolderRepository(folderDao: FolderDao): FolderRepository
        = FolderRepositoryImpl(folderDao)

    @Provides
    @Singleton
    fun provideFolderDao(appDatabase: AppDatabase): FolderDao = appDatabase.folderDao()

    @Singleton
    @Provides
    fun provideRecordRepository(recordDao: RecordDao): RecordRepository =
        RecordRepositoryImpl(recordDao)

    @Singleton
    @Provides
    fun provideRecordDao(appDatabase: AppDatabase): RecordDao = appDatabase.recordDao()

    @Singleton
    @Provides
    fun provideBoardRepository(boardDao: BoardDao): BoardRepository = BoardRepositoryImpl(boardDao)

    @Singleton
    @Provides
    fun provideBoardDao(appDatabase: AppDatabase): BoardDao = appDatabase.boardDao()


    @Singleton
    @Provides
    fun provideSavedGameRepository(savedGameDao: SavedGameDao): SavedGameRepository =
        SavedGameRepositoryImpl(savedGameDao)

    @Singleton
    @Provides
    fun provideSavedGameDao(appDatabase: AppDatabase): SavedGameDao = appDatabase.savedGameDao()


    @Provides
    @Singleton
    fun provideAppSettingsManager(@ApplicationContext context: Context) =
        AppSettingsManager(context)

    @Provides
    @Singleton
    fun provideThemeSettingsManager(@ApplicationContext context: Context) =
        ThemeSettingsManager(context)

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase = AppDatabase.getInstance(context = app)
}