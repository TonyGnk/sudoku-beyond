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

package gr.tonygnk.sudokubeyond.ui.learn.learnsudoku

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.Cell
import gr.tonygnk.sudokubeyond.core.Note
import gr.tonygnk.sudokubeyond.core.qqwing.GameType
import gr.tonygnk.sudokubeyond.core.utils.SudokuParser
import gr.tonygnk.sudokubeyond.ui.components.AnimatedNavigation
import gr.tonygnk.sudokubeyond.ui.components.board.Board
import gr.tonygnk.sudokubeyond.ui.learn.components.TutorialBase
import gr.tonygnk.sudokubeyond.ui.learn.components.TutorialBottomContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(style = AnimatedNavigation::class)
@Composable
fun LearnHiddenPairs(
    navigator: DestinationsNavigator
) {
    TutorialBase(
        title = stringResource(R.string.learn_hidden_pairs_title),
        navigator = navigator
    ) {
        val sudokuParser = SudokuParser()
        val board by remember {
            mutableStateOf(
                sudokuParser.parseBoard(
                    "..............................3.1.......2........................................",
                    GameType.Default9x9,
                    emptySeparator = '.'
                ).toList()
            )
        }
        var notes by remember {
            mutableStateOf(
                listOf(
                    Note(3, 4, 4),
                    Note(3, 4, 5),
                    Note(3, 4, 8),
                    Note(4, 3, 4),
                    Note(4, 3, 5),
                    Note(4, 3, 7),
                    Note(4, 5, 4),
                    Note(4, 5, 5),
                    Note(5, 3, 6),
                    Note(5, 3, 7),
                    Note(5, 3, 8),
                    Note(5, 3, 9),
                    Note(5, 4, 7),
                    Note(5, 4, 8),
                    Note(5, 5, 6),
                    Note(5, 5, 8),
                    Note(5, 5, 9),
                )
            )
        }
        val steps = listOf(
            stringResource(R.string.learn_hidden_pairs_1),
            stringResource(R.string.learn_hidden_pairs_2)
        )
        val stepsCell = listOf(
            listOf(Cell(5, 3), Cell(5, 5))
        )
        var step by remember { mutableIntStateOf(0) }
        LaunchedEffect(key1 = step) {
            when (step) {
                0 -> {
                    notes = listOf(
                        Note(3, 4, 4),
                        Note(3, 4, 5),
                        Note(3, 4, 8),
                        Note(4, 3, 4),
                        Note(4, 3, 5),
                        Note(4, 3, 7),
                        Note(4, 5, 4),
                        Note(4, 5, 5),
                        Note(5, 3, 6),
                        Note(5, 3, 7),
                        Note(5, 3, 8),
                        Note(5, 3, 9),
                        Note(5, 4, 7),
                        Note(5, 4, 8),
                        Note(5, 5, 6),
                        Note(5, 5, 8),
                        Note(5, 5, 9)
                    )
                }

                1 -> {
                    notes = listOf(
                        Note(3, 4, 4),
                        Note(3, 4, 5),
                        Note(3, 4, 8),
                        Note(4, 3, 4),
                        Note(4, 3, 5),
                        Note(4, 3, 7),
                        Note(4, 5, 4),
                        Note(4, 5, 5),
                        Note(5, 3, 6),
                        Note(5, 3, 9),
                        Note(5, 4, 7),
                        Note(5, 4, 8),
                        Note(5, 5, 6),
                        Note(5, 5, 9)
                    )
                }
            }
        }

        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (isLandscape) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Board(
                    board = board,
                    notes = notes,
                    cellsToHighlight = if (step < stepsCell.size) stepsCell[step] else null,
                    onClick = { },
                    selectedCell = Cell(-1, -1)
                )
                TutorialBottomContent(
                    steps = steps,
                    step = step,
                    onPreviousClick = { if (step > 0) step-- },
                    onNextClick = { if (step < (steps.size - 1)) step++ },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Board(
                    board = board,
                    notes = notes,
                    cellsToHighlight = if (step < stepsCell.size) stepsCell[step] else null,
                    onClick = { },
                    selectedCell = Cell(-1, -1)
                )
                TutorialBottomContent(
                    steps = steps,
                    step = step,
                    onPreviousClick = { if (step > 0) step-- },
                    onNextClick = { if (step < (steps.size - 1)) step++ }
                )
            }
        }
    }
}