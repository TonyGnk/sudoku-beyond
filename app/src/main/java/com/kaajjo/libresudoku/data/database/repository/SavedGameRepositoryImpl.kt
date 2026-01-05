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

import com.kaajjo.libresudoku.data.database.dao.SavedGameDao
import com.kaajjo.libresudoku.data.database.model.SavedGame
import com.kaajjo.libresudoku.data.database.model.SudokuBoard
import com.kaajjo.libresudoku.domain.repository.SavedGameRepository
import kotlinx.coroutines.flow.Flow

class SavedGameRepositoryImpl(
    private val savedGameDao: SavedGameDao
) : SavedGameRepository {
    override fun getAll(): Flow<List<SavedGame>> = savedGameDao.getAll()

    override suspend fun get(uid: Long): SavedGame? = savedGameDao.get(uid)

    override fun getWithBoards(): Flow<Map<SavedGame, SudokuBoard>> = savedGameDao.getSavedWithBoards()

    override fun getLast(): Flow<SavedGame?> = savedGameDao.getLast()

    override fun getLastPlayable(limit: Int): Flow<Map<SavedGame, SudokuBoard>> = savedGameDao.getLastPlayable(limit)

    override suspend fun insert(savedGame: SavedGame): Long = savedGameDao.insert(savedGame)

    override suspend fun insert(savedGames: List<SavedGame>) = savedGameDao.insert(savedGames)

    override suspend fun update(savedGame: SavedGame) = savedGameDao.update(savedGame)

    override suspend fun delete(savedGame: SavedGame) = savedGameDao.delete(savedGame)
}