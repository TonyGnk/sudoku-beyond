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

package gr.tonygnk.sudokubeyond.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gr.tonygnk.sudokubeyond.core.qqwing.GameDifficulty
import gr.tonygnk.sudokubeyond.core.qqwing.GameType
import gr.tonygnk.sudokubeyond.data.database.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT * FROM record WHERE board_uid == :uid")
    suspend fun get(uid: Long): Record

    @Query("SELECT * FROM record")
    fun getAll(): Flow<List<Record>>

    @Query(
        "SELECT * FROM record " +
                "WHERE type == :type and difficulty == :difficulty " +
                "ORDER BY time ASC"
    )
    fun getAll(difficulty: GameDifficulty, type: GameType): Flow<List<Record>>

    @Query("SELECT * FROM record ORDER BY time ASC")
    fun getAllSortByTime(): Flow<List<Record>>

    @Delete
    suspend fun delete(record: Record)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: Record)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(records: List<Record>)
}