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

package com.kaajjo.libresudoku.data.database.repository

import com.kaajjo.libresudoku.core.qqwing.GameDifficulty
import com.kaajjo.libresudoku.data.database.dao.BoardDao
import com.kaajjo.libresudoku.data.database.model.SavedGame
import com.kaajjo.libresudoku.data.database.model.SudokuBoard
import com.kaajjo.libresudoku.domain.repository.BoardRepository
import kotlinx.coroutines.flow.Flow

class BoardRepositoryImpl(
    private val boardDao: BoardDao
) : BoardRepository {
    override fun getAll(): Flow<List<SudokuBoard>> =
        boardDao.getAll()

    override fun getAll(difficulty: GameDifficulty): Flow<List<SudokuBoard>> =
        boardDao.getAll(difficulty)

    override fun getAllInFolder(folderUid: Long): Flow<List<SudokuBoard>> =
        boardDao.getAllInFolder(folderUid)

    override fun getAllInFolderList(folderUid: Long): List<SudokuBoard> =
        boardDao.getAllInFolderList(folderUid)

    override fun getWithSavedGames(): Flow<Map<SudokuBoard, SavedGame?>> =
        boardDao.getBoardsWithSavedGames()

    override fun getWithSavedGames(difficulty: GameDifficulty): Flow<Map<SudokuBoard, SavedGame?>> =
        boardDao.getBoardsWithSavedGames(difficulty)

    override fun getInFolderWithSaved(folderUid: Long): Flow<Map<SudokuBoard, SavedGame?>> =
        boardDao.getInFolderWithSaved(folderUid)

    override fun getBoardsInFolder(uid: Long): List<SudokuBoard> = boardDao.getBoardsInFolder(uid)
    override fun getBoardsInFolderFlow(uid: Long): Flow<List<SudokuBoard>> =
        boardDao.getBoardsInFolderFlow(uid)

    override suspend fun get(uid: Long): SudokuBoard = boardDao.get(uid)
    override suspend fun insert(boards: List<SudokuBoard>): List<Long> = boardDao.insert(boards)
    override suspend fun insert(board: SudokuBoard): Long = boardDao.insert(board)
    override suspend fun delete(board: SudokuBoard) = boardDao.delete(board)
    override suspend fun delete(boards: List<SudokuBoard>) = boardDao.delete(boards)
    override suspend fun update(board: SudokuBoard) = boardDao.update(board)
    override suspend fun update(boards: List<SudokuBoard>) = boardDao.update(boards)
}