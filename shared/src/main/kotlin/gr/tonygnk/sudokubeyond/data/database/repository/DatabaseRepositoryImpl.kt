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

package gr.tonygnk.sudokubeyond.data.database.repository

import gr.tonygnk.sudokubeyond.data.database.AppDatabase
import gr.tonygnk.sudokubeyond.domain.repository.DatabaseRepository
import kotlinx.coroutines.runBlocking

class DatabaseRepositoryImpl(
    private val appDatabase: AppDatabase
) : DatabaseRepository {
    /**
     * Completely resets database. Clearing all tables and primary key sequence
     */
    override suspend fun resetDb() {
        appDatabase.runInTransaction {
            runBlocking {
                appDatabase.clearAllTables()
                appDatabase.databaseDao().clearPrimaryKeyIndex()
            }
        }
    }
}