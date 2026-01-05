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

package gr.tonygnk.sudokubeyond.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gr.tonygnk.sudokubeyond.data.database.converters.DurationConverter
import gr.tonygnk.sudokubeyond.data.database.converters.GameDifficultyConverter
import gr.tonygnk.sudokubeyond.data.database.converters.GameTypeConverter
import gr.tonygnk.sudokubeyond.data.database.converters.ZonedDateTimeConverter
import gr.tonygnk.sudokubeyond.data.database.dao.BoardDao
import gr.tonygnk.sudokubeyond.data.database.dao.DatabaseDao
import gr.tonygnk.sudokubeyond.data.database.dao.FolderDao
import gr.tonygnk.sudokubeyond.data.database.dao.RecordDao
import gr.tonygnk.sudokubeyond.data.database.dao.SavedGameDao
import gr.tonygnk.sudokubeyond.data.database.model.Folder
import gr.tonygnk.sudokubeyond.data.database.model.Record
import gr.tonygnk.sudokubeyond.data.database.model.SavedGame
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard

@Database(
    entities = [Record::class, SudokuBoard::class, SavedGame::class, Folder::class],
    version = 6,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6)
    ]
)
@TypeConverters(
    DurationConverter::class,
    ZonedDateTimeConverter::class,
    GameDifficultyConverter::class,
    GameTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
    abstract fun boardDao(): BoardDao
    abstract fun savedGameDao(): SavedGameDao

    abstract fun folderDao(): FolderDao

    abstract  fun databaseDao(): DatabaseDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "main_database"
                ).build()
            }

            return INSTANCE as AppDatabase
        }
    }
}