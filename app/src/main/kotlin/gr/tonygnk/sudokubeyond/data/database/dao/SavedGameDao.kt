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
import androidx.room.Update
import gr.tonygnk.sudokubeyond.data.database.model.SavedGame
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedGameDao {
    @Query("SELECT * FROM saved_game")
    fun getAll(): Flow<List<SavedGame>>

    @Query("SELECT * FROM saved_game WHERE board_uid == :uid")
    suspend fun get(uid: Long): SavedGame?

    @Query(
        "SELECT * FROM saved_game " +
                "JOIN board ON saved_game.board_uid == board.uid " +
                "ORDER BY uid DESC"
    )
    fun getSavedWithBoards(): Flow<Map<SavedGame, SudokuBoard>>

    @Query(
        "SELECT * " +
                "FROM saved_game " +
                "ORDER BY board_uid DESC " +
                "LIMIT 1"
    )
    fun getLast(): Flow<SavedGame?>

    @Query(
        "SELECT * FROM saved_game " +
                "JOIN board on saved_game.board_uid == board.uid " +
                "WHERE saved_game.can_continue == 1 " +
                "ORDER BY last_played DESC " +
                "LIMIT :limit "
    )
    fun getLastPlayable(limit: Int): Flow<Map<SavedGame, SudokuBoard>>

    @Insert
    suspend fun insert(savedGame: SavedGame): Long

    @Insert
    suspend fun insert(savedGames: List<SavedGame>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(savedGame: SavedGame)

    @Delete
    suspend fun delete(savedGame: SavedGame)
}