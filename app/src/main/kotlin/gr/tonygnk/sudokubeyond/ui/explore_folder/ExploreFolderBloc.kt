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

package gr.tonygnk.sudokubeyond.ui.explore_folder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudoku.core.algorithm.QQWingController
import gr.tonygnk.sudoku.core.types.GameDifficulty
import gr.tonygnk.sudoku.core.types.GameType
import gr.tonygnk.sudoku.core.utils.SudokuParser
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard
import gr.tonygnk.sudokubeyond.domain.usecase.UpdateManyBoardsUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.board.DeleteBoardUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.board.DeleteBoardsUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.board.GetBoardsInFolderWithSavedUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.board.InsertBoardUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.board.UpdateBoardUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.folder.GetFolderUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.folder.GetFoldersUseCase
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalDecomposeApi::class)
class ExploreFolderBloc(
    blocContext: BlocContext,
    private val folderUid: Long,
    getFolderUseCase: GetFolderUseCase,
    getBoardsInFolderWithSavedUseCase: GetBoardsInFolderWithSavedUseCase,
    private val updateBoardUseCase: UpdateBoardUseCase,
    private val updateManyBoardsUseCase: UpdateManyBoardsUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase,
    private val deleteBoardsUseCase: DeleteBoardsUseCase,
    private val insertBoardUseCase: InsertBoardUseCase,
    getFoldersUseCase: GetFoldersUseCase,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {
    private val scope = lifecycle.coroutineScope

    val folder = getFolderUseCase(folderUid)
    val games = getBoardsInFolderWithSavedUseCase(folderUid)

    var gameUidToPlay: Long? by mutableStateOf(null)
    var isPlayedBefore by mutableStateOf(false)
    var readyToPlay by mutableStateOf(false)

    var inSelectionMode by mutableStateOf(false)
    var selectedBoardsList by mutableStateOf(emptyList<SudokuBoard>())

    val folders = getFoldersUseCase()

    private var _generatedSudokuCount = MutableStateFlow(0)
    val generatedSudokuCount = _generatedSudokuCount.asStateFlow()

    private var generatingJob: Job? = null

    fun prepareSudokuToPlay(board: SudokuBoard) {
        gameUidToPlay = board.uid
        if (board.solvedBoard == "") {
            scope.launch {
                val qqWingController = QQWingController()
                val sudokuParser = SudokuParser()
                val boardToSolve = board.initialBoard.map { it.digitToInt(13) }.toIntArray()

                val solved = qqWingController.solve(boardToSolve, board.type)

                if (qqWingController.solutionCount == 1) {
                    updateBoardUseCase(
                        board.copy(solvedBoard = sudokuParser.boardToString(solved))
                    )
                    readyToPlay = true
                }
            }
        } else {
            isPlayedBefore = true
            readyToPlay = true
        }
    }

    fun addToSelection(board: SudokuBoard) {
        var currentSelected = selectedBoardsList
        currentSelected = if (!currentSelected.contains(board)) {
            currentSelected + board
        } else {
            currentSelected - board
        }
        selectedBoardsList = currentSelected
    }

    fun addAllToSelection(boards: List<SudokuBoard>) {
        selectedBoardsList = boards
    }

    fun deleteSelected() {
        scope.launch(Dispatchers.IO) {
            deleteBoardsUseCase(selectedBoardsList)
            selectedBoardsList = emptyList()
            inSelectionMode = false
        }
    }

    fun deleteGame(board: SudokuBoard) {
        scope.launch {
            deleteBoardUseCase(board)
        }
    }

    fun moveBoards(folderUid: Long) {
        scope.launch {
            updateManyBoardsUseCase(
                selectedBoardsList.map { it.copy(folderId = folderUid) }
            )
            selectedBoardsList = emptyList()
        }
    }

    fun generateSudoku(type: GameType, difficulty: GameDifficulty, numberToGenerate: Int) {
        generatingJob = scope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                _generatedSudokuCount.emit(0)
            }

            val sudokuParser = SudokuParser()

            for (i in 1..numberToGenerate) {
                val qqWingController = QQWingController()
                val generatedBoard = qqWingController.generate(type, difficulty)

                val board = SudokuBoard(
                    uid = 0L,
                    type = type,
                    difficulty = difficulty,
                    initialBoard = sudokuParser.boardToString(generatedBoard),
                    solvedBoard = "",
                    folderId = folderUid
                )
                withContext(Dispatchers.IO) {
                    insertBoardUseCase(board)
                }
                withContext(Dispatchers.Main) {
                    _generatedSudokuCount.emit(i)
                }
            }
        }
    }

    fun canelGeneratingIfRunning() {
        generatingJob?.cancel()
    }

    companion object Companion {
        operator fun invoke(blocContext: BlocContext, folderUid: Long) = ExploreFolderBloc(
            blocContext = blocContext,
            folderUid = folderUid,
            getFolderUseCase = GetFolderUseCase(
                folderRepository = LibreSudokuApp.appModule.folderRepository
            ),
            getBoardsInFolderWithSavedUseCase = GetBoardsInFolderWithSavedUseCase(
                boardRepository = LibreSudokuApp.appModule.boardRepository,
            ),
            updateBoardUseCase = UpdateBoardUseCase(
                boardRepository = LibreSudokuApp.appModule.boardRepository
            ),
            updateManyBoardsUseCase = UpdateManyBoardsUseCase(
                boardRepository = LibreSudokuApp.appModule.boardRepository
            ),
            deleteBoardUseCase = DeleteBoardUseCase(
                boardRepository = LibreSudokuApp.appModule.boardRepository
            ),
            deleteBoardsUseCase = DeleteBoardsUseCase(
                boardRepository = LibreSudokuApp.appModule.boardRepository
            ),
            insertBoardUseCase = InsertBoardUseCase(
                boardRepository = LibreSudokuApp.appModule.boardRepository
            ),
            getFoldersUseCase = GetFoldersUseCase(
                folderRepository = LibreSudokuApp.appModule.folderRepository
            ),
        )
    }
}