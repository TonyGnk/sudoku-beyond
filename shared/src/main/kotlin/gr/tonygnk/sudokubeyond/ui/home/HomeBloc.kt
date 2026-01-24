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

package gr.tonygnk.sudokubeyond.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudoku.core.algorithm.CageGenerator
import gr.tonygnk.sudoku.core.algorithm.QQWingController
import gr.tonygnk.sudoku.core.model.Cage
import gr.tonygnk.sudoku.core.model.Cell
import gr.tonygnk.sudoku.core.types.GameDifficulty.Challenge
import gr.tonygnk.sudoku.core.types.GameDifficulty.Easy
import gr.tonygnk.sudoku.core.types.GameDifficulty.Hard
import gr.tonygnk.sudoku.core.types.GameDifficulty.Moderate
import gr.tonygnk.sudoku.core.types.GameType
import gr.tonygnk.sudoku.core.utils.SudokuParser
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.domain.repository.BoardRepository
import gr.tonygnk.sudokubeyond.domain.repository.SavedGameRepository
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@OptIn(ExperimentalDecomposeApi::class)
class HomeBloc(
    blocContext: BlocContext,
    private val appSettingsManager: AppSettingsManager,
    private val boardRepository: BoardRepository,
    private val savedGameRepository: SavedGameRepository,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {

    private val scope = lifecycle.coroutineScope

    val lastSavedGame = savedGameRepository.getLast()
        .stateIn(scope, SharingStarted.Eagerly, null)

    private val lastGamesLimit = 5
    val lastGames = savedGameRepository.getLastPlayable(limit = lastGamesLimit)
        .stateIn(scope, SharingStarted.Eagerly, emptyMap())

    var insertedBoardUid = -1L

    private val difficulties = listOf(Easy, Moderate, Hard, Challenge)

    private val types = listOf(
        GameType.Default9x9,
        GameType.Default6x6,
        GameType.Default12x12,
        GameType.Killer9x9,
        GameType.Killer12x12,
        GameType.Killer6x6
    )

    val lastSelectedGameDifficultyType = appSettingsManager.lastSelectedGameDifficultyType
    val saveSelectedGameDifficultyType = appSettingsManager.saveSelectedGameDifficultyType

    var selectedDifficulty by mutableStateOf(difficulties.first())
    var selectedType by mutableStateOf(types.first())

    var isGenerating by mutableStateOf(false)
    var isSolving by mutableStateOf(false)
    var readyToPlay by mutableStateOf(false)

    private var puzzle =
        List(selectedType.size) { row -> List(selectedType.size) { col -> Cell(row, col, 0) } }
    private var solvedPuzzle =
        List(selectedType.size) { row -> List(selectedType.size) { col -> Cell(row, col, 0) } }


    fun startGame() {
        isSolving = false
        isGenerating = false

        val gameTypeToGenerate = selectedType
        val gameDifficultyToGenerate = selectedDifficulty
        val size = gameTypeToGenerate.size

        puzzle = List(size) { row -> List(size) { col -> Cell(row, col, 0) } }
        solvedPuzzle = List(size) { row -> List(size) { col -> Cell(row, col, 0) } }

        scope.launch(Dispatchers.Default) {
            val saveSelectedGameDifficultyAndType = runBlocking { appSettingsManager.saveSelectedGameDifficultyType.first() }
            if (saveSelectedGameDifficultyAndType) {
                appSettingsManager.setLastSelectedGameDifficultyType(
                    difficulty = selectedDifficulty,
                    type = selectedType
                )
            }

            val qqWingController = QQWingController()

            // generating
            isGenerating = true
            val generated = qqWingController.generate(gameTypeToGenerate, gameDifficultyToGenerate)
            isGenerating = false

            isSolving = true
            val solved = qqWingController.solve(generated, gameTypeToGenerate)
            isSolving = false


            if (!qqWingController.isImpossible && qqWingController.solutionCount == 1) {
                for (i in 0 until size) {
                    for (j in 0 until size) {
                        puzzle[i][j].value = generated[i * size + j]
                        solvedPuzzle[i][j].value = solved[i * size + j]
                    }
                }

                var cages: List<Cage>? = null
                if (gameTypeToGenerate in setOf(
                        GameType.Killer9x9,
                        GameType.Killer12x12,
                        GameType.Killer6x6
                    )
                ) {
                    val generator = CageGenerator(solvedPuzzle, gameTypeToGenerate)
                    cages = generator.generate(2, 5)
                }
                withContext(Dispatchers.IO) {
                    val sudokuParser = SudokuParser()
                    insertedBoardUid = boardRepository.insert(
                        SudokuBoard(
                            uid = 0,
                            initialBoard = sudokuParser.boardToString(puzzle),
                            solvedBoard = sudokuParser.boardToString(solvedPuzzle),
                            difficulty = selectedDifficulty,
                            type = selectedType,
                            killerCages = if (cages != null) sudokuParser.killerSudokuCagesToString(
                                cages
                            ) else null
                        )
                    )
                }

                readyToPlay = true
            }
        }
    }

    fun changeDifficulty(diff: Int) {
        val indexToSet = difficulties.indexOf(selectedDifficulty) + diff
        if (indexToSet >= 0 && indexToSet < difficulties.count()) {
            selectedDifficulty = difficulties[indexToSet]
        }
    }

    fun changeType(diff: Int) {
        val indexToSet = types.indexOf(selectedType) + diff
        if (indexToSet >= 0 && indexToSet < types.count()) {
            selectedType = types[indexToSet]
        }
    }

    fun giveUpLastGame() {
        scope.launch(Dispatchers.IO) {
            lastSavedGame.value?.let {
                if (!it.completed) {
                    savedGameRepository.update(
                        it.copy(
                            completed = true,
                            canContinue = true
                        )
                    )
                }
            }
        }
    }

    companion object Companion {
        operator fun invoke(blocContext: BlocContext) = HomeBloc(
            blocContext = blocContext,
            appSettingsManager = LibreSudokuApp.appModule.appSettingsManager,
            boardRepository = LibreSudokuApp.appModule.boardRepository,
            savedGameRepository = LibreSudokuApp.appModule.savedGameRepository
        )
    }
}