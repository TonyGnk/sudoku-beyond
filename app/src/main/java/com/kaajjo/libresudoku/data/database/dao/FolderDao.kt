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

package com.kaajjo.libresudoku.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kaajjo.libresudoku.data.database.model.Folder
import com.kaajjo.libresudoku.data.database.model.SavedGame
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM Folder")
    fun get(): Flow<List<Folder>>

    @Query("SELECT * FROM Folder WHERE uid = :uid")
    fun get(uid: Long): Flow<Folder>

    @Query("SELECT COUNT(uid) FROM board WHERE folder_id == :uid")
    fun countPuzzlesFolder(uid: Long): Long

    @Query(
        "SELECT * FROM saved_game" +
                " INNER JOIN board ON board.folder_id NOT NULL AND board_uid = board.uid AND can_continue" +
                " ORDER BY last_played DESC" +
                " LIMIT :gamesCount"
    )
    fun getLastSavedGamesAnyFolder(gamesCount: Int): Flow<List<SavedGame>>


    @Insert
    suspend fun insert(folder: Folder): Long

    @Insert
    suspend fun insert(folders: List<Folder>): List<Long>

    @Update
    suspend fun update(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)
}