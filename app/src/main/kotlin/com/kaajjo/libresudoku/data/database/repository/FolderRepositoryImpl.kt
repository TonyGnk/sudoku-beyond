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

import com.kaajjo.libresudoku.data.database.dao.FolderDao
import com.kaajjo.libresudoku.data.database.model.Folder
import com.kaajjo.libresudoku.data.database.model.SavedGame
import com.kaajjo.libresudoku.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow

class FolderRepositoryImpl(
    private val folderDao: FolderDao
) : FolderRepository {
    override fun getAll(): Flow<List<Folder>> = folderDao.get()

    override fun get(uid: Long): Flow<Folder> = folderDao.get(uid)

    override fun countPuzzlesFolder(uid: Long): Long = folderDao.countPuzzlesFolder(uid)

    override fun getLastSavedGamesAnyFolder(gamesCount: Int): Flow<List<SavedGame>> = folderDao.getLastSavedGamesAnyFolder(gamesCount)

    override suspend fun insert(folder: Folder): Long = folderDao.insert(folder)

    override suspend fun insert(folders: List<Folder>): List<Long> = folderDao.insert(folders)

    override suspend fun update(folder: Folder) = folderDao.update(folder)

    override suspend fun delete(folder: Folder) = folderDao.delete(folder)
}