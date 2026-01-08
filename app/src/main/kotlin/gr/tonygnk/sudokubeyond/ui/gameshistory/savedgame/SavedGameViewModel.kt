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

package gr.tonygnk.sudokubeyond.ui.gameshistory.savedgame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudoku.core.model.Cage
import gr.tonygnk.sudoku.core.model.Cell
import gr.tonygnk.sudoku.core.model.Note
import gr.tonygnk.sudoku.core.utils.SudokuParser
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.data.database.model.Folder
import gr.tonygnk.sudokubeyond.data.database.model.SavedGame
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.domain.repository.BoardRepository
import gr.tonygnk.sudokubeyond.domain.repository.SavedGameRepository
import gr.tonygnk.sudokubeyond.domain.usecase.folder.GetFolderUseCase
import gr.tonygnk.sudokubeyond.navArgs
import gr.tonygnk.sudokubeyond.ui.util.SudokuUIUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow

class SavedGameViewModel(
    private val boardRepository: BoardRepository,
    private val savedGameRepository: SavedGameRepository,
    private val getFolderUseCase: GetFolderUseCase,
    appSettingsManager: AppSettingsManager,
    themeSettingsManager: ThemeSettingsManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val navArgs: SavedGameScreenNavArgs = savedStateHandle.navArgs()
    val boardUid = navArgs.gameUid

    val fontSize = appSettingsManager.fontSize
    val dateFormat = appSettingsManager.dateFormat

    var savedGame by mutableStateOf<SavedGame?>(null)
    var boardEntity by mutableStateOf<SudokuBoard?>(null)

    var parsedInitialBoard by mutableStateOf(emptyList<List<Cell>>())
    var parsedCurrentBoard by mutableStateOf(emptyList<List<Cell>>())
    var killerCages by mutableStateOf(emptyList<Cage>())
    var notes by mutableStateOf(emptyList<Note>())

    var exportDialog by mutableStateOf(false)

    private val _gameFolder: MutableStateFlow<Folder?> = MutableStateFlow(null)
    val gameFolder = _gameFolder.asStateFlow()

    private val _gameProgressPercentage = MutableStateFlow(0)
    val gameProgressPercentage = _gameProgressPercentage.asStateFlow()

    val crossHighlight = themeSettingsManager.boardCrossHighlight

    fun updateGameDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            boardEntity = boardRepository.get(boardUid)
            savedGame = savedGameRepository.get(boardUid)

            boardEntity?.let { boardEntity ->
                savedGame?.let { savedGame ->
                    withContext(Dispatchers.Default) {
                        val sudokuParser = SudokuParser()
                        parsedInitialBoard =
                            sudokuParser.parseBoard(
                                boardEntity.initialBoard,
                                boardEntity.type,
                                locked = true
                            )
                        parsedCurrentBoard =
                            sudokuParser.parseBoard(savedGame.currentBoard, boardEntity.type)
                                .onEach { cells ->
                                    cells.forEach { cell ->
                                        cell.locked =
                                            parsedInitialBoard[cell.row][cell.col].value != 0
                                    }
                                }
                        notes = sudokuParser.parseNotes(savedGame.notes)
                        if (boardEntity.killerCages != null) {
                            killerCages = sudokuParser.parseKillerSudokuCages(boardEntity.killerCages)
                        }
                    }
                }

                viewModelScope.launch {
                    boardEntity.folderId?.let { folderUid ->
                        val folder = getFolderUseCase(folderUid)
                        folder.collectLatest {
                            _gameFolder.emit(it)
                        }
                    }
                }
            }
        }
    }

    fun countProgressFilled() {
        viewModelScope.launch {
            var totalCells = 1
            var count = 0
            boardEntity?.let { board ->
                totalCells = (board.type.sectionWidth * board.type.sectionHeight)
                    .toDouble()
                    .pow(2.0)
                    .toInt()
                count =
                    totalCells - parsedCurrentBoard.sumOf { cells -> cells.count { cell -> cell.value == 0 } }
            }
            _gameProgressPercentage.emit((count.toFloat() / totalCells.toFloat() * 100f).toInt())
        }
    }

    fun getFontSize(factor: Int): TextUnit {
        boardEntity?.let {
            return SudokuUIUtils.getFontSize(it.type, factor)
        }
        return 24.sp
    }

    companion object {
        val builder: (SavedStateHandle) -> SavedGameViewModel = { savedStateHandle ->
            SavedGameViewModel(
                boardRepository = LibreSudokuApp.appModule.boardRepository,
                savedGameRepository = LibreSudokuApp.appModule.savedGameRepository,
                getFolderUseCase = GetFolderUseCase(LibreSudokuApp.appModule.folderRepository),
                appSettingsManager = LibreSudokuApp.appModule.appSettingsManager,
                themeSettingsManager = LibreSudokuApp.appModule.themeSettingsManager,
                savedStateHandle = savedStateHandle
            )
        }
    }
}