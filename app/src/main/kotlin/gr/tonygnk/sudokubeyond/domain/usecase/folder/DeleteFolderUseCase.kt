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

package gr.tonygnk.sudokubeyond.domain.usecase.folder

import gr.tonygnk.sudokubeyond.data.database.model.Folder
import gr.tonygnk.sudokubeyond.domain.repository.FolderRepository

class DeleteFolderUseCase(
    private val folderRepository: FolderRepository,
) {
    suspend operator fun invoke(folder: Folder) = folderRepository.delete(folder)
}