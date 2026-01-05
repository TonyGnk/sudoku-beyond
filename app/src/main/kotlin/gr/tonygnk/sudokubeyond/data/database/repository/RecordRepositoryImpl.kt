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

import gr.tonygnk.sudokubeyond.core.qqwing.GameDifficulty
import gr.tonygnk.sudokubeyond.core.qqwing.GameType
import gr.tonygnk.sudokubeyond.data.database.dao.RecordDao
import gr.tonygnk.sudokubeyond.data.database.model.Record
import gr.tonygnk.sudokubeyond.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow

class RecordRepositoryImpl(
    private val recordDao: RecordDao
) : RecordRepository {
    override suspend fun get(uid: Long): Record = recordDao.get(uid)
    override fun getAll(): Flow<List<Record>> = recordDao.getAll()
    override fun getAllSortByTime(): Flow<List<Record>> = recordDao.getAllSortByTime()
    override fun getAll(difficulty: GameDifficulty, type: GameType) = recordDao.getAll(difficulty, type)
    override suspend fun insert(record: Record) = recordDao.insert(record)
    override suspend fun insert(records: List<Record>) = recordDao.insert(records)
    override suspend fun delete(record: Record) = recordDao.delete(record)
}