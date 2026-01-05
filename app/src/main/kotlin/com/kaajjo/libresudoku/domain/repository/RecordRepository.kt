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

package com.kaajjo.libresudoku.domain.repository

import com.kaajjo.libresudoku.core.qqwing.GameDifficulty
import com.kaajjo.libresudoku.core.qqwing.GameType
import com.kaajjo.libresudoku.data.database.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun get(uid: Long): Record
    fun getAll(): Flow<List<Record>>
    fun getAllSortByTime(): Flow<List<Record>>
    fun getAll(difficulty: GameDifficulty, type: GameType): Flow<List<Record>>
    suspend fun insert(record: Record)
    suspend fun insert(records: List<Record>)
    suspend fun delete(record: Record)
}