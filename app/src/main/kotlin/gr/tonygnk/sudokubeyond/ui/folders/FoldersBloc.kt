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

package gr.tonygnk.sudokubeyond.ui.folders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.core.parser.SdmParser
import gr.tonygnk.sudokubeyond.data.database.model.Folder
import gr.tonygnk.sudokubeyond.domain.usecase.board.GetGamesInFolderUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.folder.CountPuzzlesFolderUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.folder.DeleteFolderUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.folder.GetFoldersUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.folder.GetLastSavedGamesAnyFolderUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.folder.InsertFolderUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.folder.UpdateFolderUseCase
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@OptIn(ExperimentalDecomposeApi::class)
class FoldersBloc(
    blocContext: BlocContext,
    getFoldersUseCase: GetFoldersUseCase,
    private val insertFolderUseCase: InsertFolderUseCase,
    private val updateFolderUseCase: UpdateFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val getGamesInFolderUseCase: GetGamesInFolderUseCase,
    private val countPuzzlesFolderUseCase: CountPuzzlesFolderUseCase,
    getLastSavedGamesAnyFolderUseCase: GetLastSavedGamesAnyFolderUseCase,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {

    private val scope = lifecycle.coroutineScope

    val folders = getFoldersUseCase()

    var selectedFolder: Folder? by mutableStateOf(null)

    private val _sudokuListToImport = MutableStateFlow(emptyList<String>())
    val sudokuListToImport = _sudokuListToImport.asStateFlow()

    var puzzlesCountInFolder by mutableStateOf(emptyList<Pair<Long, Int>>())

    val lastSavedGames = getLastSavedGamesAnyFolderUseCase(gamesCount = 10)

    fun createFolder(name: String) {
        scope.launch {
            insertFolderUseCase(
                Folder(
                    uid = 0,
                    name = name.trim(),
                    createdAt = ZonedDateTime.now()
                )
            )
        }
    }

    fun countPuzzlesInFolders(folders: List<Folder>) {
        scope.launch(Dispatchers.IO) {
            val numberPuzzles = mutableListOf<Pair<Long, Int>>()
            folders.forEach {
                numberPuzzles.add(
                    Pair(it.uid, countPuzzlesFolderUseCase(it.uid).toInt())
                )
            }
            puzzlesCountInFolder = numberPuzzles
        }
    }

    fun renameFolder(newName: String) {
        selectedFolder?.let {
            scope.launch {
                updateFolderUseCase(
                    it.copy(name = newName.trim())
                )
            }
        }
    }

    fun deleteFolder() {
        selectedFolder?.let {
            scope.launch {
                deleteFolderUseCase(it)
            }
        }
    }

    fun generateFolderExportData(): ByteArray {
        val gamesInFolder = getGamesInFolderUseCase(selectedFolder!!.uid)

        val sdmParser = SdmParser()
        return sdmParser.boardsToSdm(gamesInFolder).toByteArray()
    }

    companion object Companion {
        operator fun invoke(blocContext: BlocContext) = FoldersBloc(
            blocContext = blocContext,
            getFoldersUseCase = GetFoldersUseCase(
                folderRepository = LibreSudokuApp.appModule.folderRepository
            ),
            insertFolderUseCase = InsertFolderUseCase(
                folderRepository = LibreSudokuApp.appModule.folderRepository
            ),
            updateFolderUseCase = UpdateFolderUseCase(
                folderRepository = LibreSudokuApp.appModule.folderRepository
            ),
            deleteFolderUseCase = DeleteFolderUseCase(
                folderRepository = LibreSudokuApp.appModule.folderRepository
            ),
            getGamesInFolderUseCase = GetGamesInFolderUseCase(
                boardRepository = LibreSudokuApp.appModule.boardRepository
            ),
            countPuzzlesFolderUseCase = CountPuzzlesFolderUseCase(
                folderRepository = LibreSudokuApp.appModule.folderRepository
            ),
            getLastSavedGamesAnyFolderUseCase = GetLastSavedGamesAnyFolderUseCase(
                folderRepository = LibreSudokuApp.appModule.folderRepository
            )
        )
    }
}