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
fun LearnBasic(
    navigator: DestinationsNavigator
) {
    TutorialBase(
        title = stringResource(R.string.learn_basic_title),
        navigator = navigator
    ) {
        val sudokuParser = SudokuParser()
        var board by remember {
            mutableStateOf(
                sudokuParser.parseBoard(
                    board = "2...7..38.....6.7.3...4.6....8.2.7..1.......6..7.3.4....4.8...9.6.4.....91..6...2",
                    gameType = GameType.Default9x9,
                    emptySeparator = '.'
                ).toList()
            )
        }
        val steps = listOf(
            stringResource(R.string.learn_basic_1),
            stringResource(R.string.learn_basic_2),
            stringResource(R.string.learn_basic_3),
            stringResource(R.string.learn_basic_4),
            stringResource(R.string.learn_basic_5),
            stringResource(R.string.learn_basic_6),
        )
        val stepsCell = listOf(
            listOf(
                Cell(6, 0),
                Cell(6, 1),
                Cell(6, 2),
                Cell(7, 0),
                Cell(7, 1),
                Cell(7, 2),
                Cell(8, 0),
                Cell(8, 1),
                Cell(8, 2),
            ),
            listOf(Cell(3, 2), Cell(7, 2), Cell(8, 2)),
            listOf(Cell(6, 4), Cell(6, 0), Cell(6, 1)),
            listOf(Cell(7, 0)),
            listOf(
                Cell(6, 2),
                Cell(2, 4),
                Cell(5, 6),
                Cell(0, 2),
                Cell(0, 3),
                Cell(0, 5),
                Cell(0, 6),
            ),
            listOf(Cell(0, 1))
        )
        var step by remember { mutableIntStateOf(0) }
        LaunchedEffect(key1 = step) {
            when (step) {
                0 -> board = sudokuParser.parseBoard(
                    "2...7..38.....6.7.3...4.6....8.2.7..1.......6..7.3.4....4.8...9.6.4.....91..6...2",
                    GameType.Default9x9,
                    emptySeparator = '.'
                )

                3 -> board = sudokuParser.parseBoard(
                    "2...7..38.....6.7.3...4.6....8.2.7..1.......6..7.3.4....4.8...986.4.....91..6...2",
                    GameType.Default9x9,
                    emptySeparator = '.'
                )

                5 -> board = sudokuParser.parseBoard(
                    "24..7..38.....6.7.3...4.6....8.2.7..1.......6..7.3.4....4.8...986.4.....91..6...2",
                    GameType.Default9x9,
                    emptySeparator = '.'
                )
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