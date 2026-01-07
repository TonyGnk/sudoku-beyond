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

package gr.tonygnk.sudokubeyond.ui.create_edit_sudoku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.Cell
import gr.tonygnk.sudokubeyond.core.PreferencesConstants
import gr.tonygnk.sudokubeyond.core.qqwing.GameDifficulty
import gr.tonygnk.sudokubeyond.core.qqwing.GameType
import gr.tonygnk.sudokubeyond.core.qqwing.QQWingController
import gr.tonygnk.sudokubeyond.core.utils.GameState
import gr.tonygnk.sudokubeyond.core.utils.SudokuParser
import gr.tonygnk.sudokubeyond.core.utils.SudokuUtils
import gr.tonygnk.sudokubeyond.core.utils.UndoRedoManager
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.domain.usecase.board.GetBoardUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.board.InsertBoardUseCase
import gr.tonygnk.sudokubeyond.domain.usecase.board.UpdateBoardUseCase
import gr.tonygnk.sudokubeyond.navArgs
import gr.tonygnk.sudokubeyond.ui.game.components.ToolBarItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateSudokuViewModel(
    appSettingsManager: AppSettingsManager,
    themeSettingsManager: ThemeSettingsManager,
    private val getBoardUseCase: GetBoardUseCase,
    private val updateBoardUseCase: UpdateBoardUseCase,
    private val insertBoardUseCase: InsertBoardUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _navArgs: CreateSudokuScreenNavArgs = savedStateHandle.navArgs()
    val gameUid = _navArgs.gameUid
    private val folderUid = _navArgs.folderUid

    init {
        if (gameUid != -1L) {
            viewModelScope.launch(Dispatchers.IO) {
                val board = getBoardUseCase(gameUid)
                withContext(Dispatchers.Default) {
                    val sudokuParser = SudokuParser()
                    val parsedBoard = sudokuParser.parseBoard(
                        board = board.initialBoard,
                        gameType = board.type
                    )
                    withContext(Dispatchers.Main) {
                        gameBoard = parsedBoard
                        gameDifficulty = board.difficulty
                        gameType = board.type
                    }
                }
            }
        }
    }

    val highlightIdentical = appSettingsManager.highlightIdentical
    private val inputMethod = appSettingsManager.inputMethod
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PreferencesConstants.DEFAULT_INPUT_METHOD
        )

    val positionLines = appSettingsManager.positionLines
    val crossHighlight = themeSettingsManager.boardCrossHighlight
    val funKeyboardOverNum = appSettingsManager.funKeyboardOverNumbers

    val fontSize = appSettingsManager.fontSize

    var multipleSolutionsDialog by mutableStateOf(false)
    var noSolutionsDialog by mutableStateOf(false)


    var gameType by mutableStateOf(GameType.Default9x9)
    var gameDifficulty by mutableStateOf(GameDifficulty.Easy)
    var gameBoard by mutableStateOf(List(gameType.size) { row ->
        List(gameType.size) { col ->
            Cell(
                row,
                col,
                0
            )
        }
    })
    var currCell by mutableStateOf(Cell(-1, -1, 0))

    var importStringValue by mutableStateOf("")
    var importTextFieldError by mutableStateOf(false)

    private val sudokuUtils = SudokuUtils()
    private val undoRedoManager = UndoRedoManager(GameState(gameBoard, emptyList()))

    private var overrideInputMethodDF = false
    var digitFirstNumber = -1

    private fun getBoardNoRef(): List<List<Cell>> =
        gameBoard.map { items -> items.map { item -> item.copy() } }

    fun getFontSize(type: GameType = gameType, factor: Int): TextUnit =
        sudokuUtils.getFontSize(type, factor)

    fun processInput(cell: Cell): Boolean {
        currCell =
            if (currCell.row == cell.row && currCell.col == cell.col && digitFirstNumber == 0) {
                Cell(-1, -1)
            } else {
                cell
            }

        return if (currCell.row >= 0 && currCell.col >= 0) {
            if ((inputMethod.value == 1 || overrideInputMethodDF) && digitFirstNumber > 0) {
                processNumberInput(digitFirstNumber)
                undoRedoManager.addState(GameState(getBoardNoRef(), emptyList()))
            }
            true
        } else {
            false
        }
    }

    fun processInputKeyboard(number: Int, longTap: Boolean = false) {
        if (!longTap) {
            if (inputMethod.value == 0) {
                overrideInputMethodDF = false
                digitFirstNumber = 0
                processNumberInput(number)
                undoRedoManager.addState(GameState(getBoardNoRef(), emptyList()))
            } else if (inputMethod.value == 1) {
                digitFirstNumber = if (digitFirstNumber == number) 0 else number
                currCell = Cell(-1, -1, digitFirstNumber)
            }
        } else {
            if (inputMethod.value == 0) {
                overrideInputMethodDF = true
                digitFirstNumber = if (digitFirstNumber == number) 0 else number
                currCell = Cell(-1, -1, digitFirstNumber)
            }
        }
    }


    private fun processNumberInput(number: Int) {
        if (currCell.row >= 0 && currCell.col >= 0) {
            gameBoard = setValueCell(
                if (gameBoard[currCell.row][currCell.col].value == number) 0 else number
            )
        }
    }

    private fun setValueCell(
        value: Int,
        row: Int = currCell.row,
        col: Int = currCell.col,
    ): List<List<Cell>> {
        val new = getBoardNoRef()
        new[row][col].value = value

        if (currCell.row == row && currCell.col == col) {
            currCell = currCell.copy(value = new[row][col].value)
        }

        if (value == 0) {
            new[row][col].error = false
            currCell.error = false
            return new
        }

        new[row][col].error = !sudokuUtils.isValidCellDynamic(new, new[row][col], gameType)
        new.forEach { cells ->
            cells.forEach { cell ->
                if (cell.value != 0 && cell.error) {
                    cell.error = !sudokuUtils.isValidCellDynamic(new, cell, gameType)
                }
            }
        }

        return new
    }

    fun toolbarClick(item: ToolBarItem) {
        when (item) {
            ToolBarItem.Undo -> {
                if (undoRedoManager.canUndo()) {
                    gameBoard = undoRedoManager.undo().board
                    checkMistakes()
                }
            }

            ToolBarItem.Redo -> {
                if (undoRedoManager.canRedo()) {
                    undoRedoManager.redo()?.let {
                        gameBoard = it.board
                    }
                    checkMistakes()
                }
            }

            ToolBarItem.Remove -> {
                if (currCell.row >= 0 && currCell.col >= 0 && !currCell.locked) {
                    val prevValue = gameBoard[currCell.row][currCell.col].value
                    gameBoard = setValueCell(0)
                    if (prevValue != 0) {
                        undoRedoManager.addState(GameState(getBoardNoRef(), emptyList()))
                    }
                    checkMistakes()
                }
            }

            else -> {}
        }
    }

    fun changeGameType(gameType: GameType) {
        if (this.gameType != gameType) {
            this.gameType = gameType
            currCell = Cell(-1, -1, 0)
            digitFirstNumber = -1
            gameBoard =
                List(gameType.size) { row -> List(gameType.size) { col -> Cell(row, col, 0) } }
        }
    }

    fun setFromString(puzzle: String): Boolean {
        val sudokuParser = SudokuParser()
        if (puzzle.length !in setOf(36, 81, 144)) {
            return false
        }
        if (puzzle.all { char ->
                if (char != '0' && char != '.') {
                    try {
                        char.digitToInt(16)
                        true
                    } catch (e: Exception) {
                        false
                    }
                } else {
                    true
                }
            }
        ) {
            gameType = when (puzzle.length) {
                36 -> GameType.Default6x6
                81 -> GameType.Default9x9
                144 -> GameType.Default12x12
                else -> GameType.Default9x9
            }
            gameBoard = sudokuParser.parseBoard(
                board = puzzle,
                gameType = gameType
            )
            return true
        } else {
            return false
        }
    }

    fun saveGame(): Boolean {
        val result = checkGame()
        when (result.second) {
            0 -> noSolutionsDialog = true
            1 -> saveToDb(result.first)
            else -> multipleSolutionsDialog = true
        }
        return result.second == 1
    }

    private fun saveToDb(board: IntArray) {
        viewModelScope.launch(Dispatchers.IO) {
            var solvedBoard: String
            var initialBoard: String
            withContext(Dispatchers.Default) {
                val sudokuParser = SudokuParser()
                initialBoard = sudokuParser.boardToString(gameBoard)
                val solvedBoardList =
                    List(gameType.size) { row -> List(gameType.size) { col -> Cell(row, col, 0) } }
                for (i in 0 until gameType.size) {
                    for (j in 0 until gameType.size) {
                        solvedBoardList[i][j].value = board[j + gameType.size * i]
                    }
                }
                solvedBoard = sudokuParser.boardToString(solvedBoardList)
            }
            if (gameUid != -1L) {
                val oldBoard = getBoardUseCase(gameUid)
                updateBoardUseCase(
                    oldBoard.copy(
                        initialBoard = initialBoard,
                        solvedBoard = solvedBoard,
                        difficulty = gameDifficulty,
                        type = gameType
                    )
                )
            } else {
                insertBoardUseCase(
                    SudokuBoard(
                        uid = 0,
                        initialBoard = initialBoard,
                        solvedBoard = solvedBoard,
                        difficulty = gameDifficulty,
                        type = gameType,
                        folderId = if (folderUid != -1L) folderUid else null
                    )
                )
            }
        }
    }

    private fun checkGame(): Pair<IntArray, Int> {
        val qqWingController = QQWingController()
        val solution = qqWingController.solve(
            gameBoard.flatten().map { cell -> cell.value }.toIntArray(),
            gameType
        )
        return Pair(solution, qqWingController.solutionCount)
    }

    fun changeGameDifficulty(gameDifficulty: GameDifficulty) {
        this.gameDifficulty = gameDifficulty
    }

    private fun checkMistakes() {
        val new = getBoardNoRef()
        for (i in new.indices) {
            for (j in new.indices) {
                if (new[i][j].value != 0) {
                    new[i][j].error = !sudokuUtils.isValidCellDynamic(
                        board = new,
                        cell = new[i][j],
                        type = gameType
                    )
                }
            }
        }
        gameBoard = new
    }

    companion object {
        val builder: (SavedStateHandle) -> CreateSudokuViewModel = { savedStateHandle ->
            CreateSudokuViewModel(
                appSettingsManager = LibreSudokuApp.appModule.appSettingsManager,
                themeSettingsManager = LibreSudokuApp.appModule.themeSettingsManager,
                getBoardUseCase = GetBoardUseCase(
                    boardRepository = LibreSudokuApp.appModule.boardRepository
                ),
                updateBoardUseCase = UpdateBoardUseCase(
                    boardRepository = LibreSudokuApp.appModule.boardRepository
                ),
                insertBoardUseCase = InsertBoardUseCase(
                    boardRepository = LibreSudokuApp.appModule.boardRepository
                ),
                savedStateHandle = savedStateHandle
            )
        }
    }
}