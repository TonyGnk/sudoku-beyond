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

package gr.tonygnk.sudokubeyond.domain.repository

import gr.tonygnk.sudoku.core.types.GameDifficulty
import gr.tonygnk.sudokubeyond.data.database.model.SavedGame
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun getAll(): Flow<List<SudokuBoard>>
    fun getAll(difficulty: GameDifficulty): Flow<List<SudokuBoard>>

    fun getAllInFolder(folderUid: Long): Flow<List<SudokuBoard>>

    fun getAllInFolderList(folderUid: Long): List<SudokuBoard>
    fun getWithSavedGames(): Flow<Map<SudokuBoard, SavedGame?>>
    fun getWithSavedGames(difficulty: GameDifficulty): Flow<Map<SudokuBoard, SavedGame?>>
    fun getInFolderWithSaved(folderUid: Long): Flow<Map<SudokuBoard, SavedGame?>>
    fun getBoardsInFolderFlow(uid: Long): Flow<List<SudokuBoard>>
    fun getBoardsInFolder(uid: Long): List<SudokuBoard>
    suspend fun get(uid: Long): SudokuBoard
    suspend fun insert(boards: List<SudokuBoard>): List<Long>
    suspend fun insert(board: SudokuBoard): Long
    suspend fun delete(board: SudokuBoard)
    suspend fun delete(boards: List<SudokuBoard>)
    suspend fun update(board: SudokuBoard)
    suspend fun update(boards: List<SudokuBoard>)
}