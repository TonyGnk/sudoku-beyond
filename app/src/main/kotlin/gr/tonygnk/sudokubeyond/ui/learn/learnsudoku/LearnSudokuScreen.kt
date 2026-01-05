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

package com.kaajjo.libresudoku.ui.learn.learnsudoku

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kaajjo.libresudoku.R
import com.kaajjo.libresudoku.destinations.LearnBasicDestination
import com.kaajjo.libresudoku.destinations.LearnHiddenPairsDestination
import com.kaajjo.libresudoku.destinations.LearnNakedPairsDestination
import com.kaajjo.libresudoku.destinations.LearnSudokuRulesDestination
import com.kaajjo.libresudoku.ui.components.AnimatedNavigation
import com.kaajjo.libresudoku.ui.learn.components.LearnRowItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(style = AnimatedNavigation::class)
@Composable
fun LearnSudokuScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn {
            item {
                LearnRowItem(
                    title = stringResource(R.string.learn_sudoku_rules),
                    onClick = { navigator.navigate(LearnSudokuRulesDestination()) }
                )
                LearnRowItem(
                    title = stringResource(R.string.learn_basic_title),
                    onClick = { navigator.navigate(LearnBasicDestination()) }
                )
                LearnRowItem(
                    title = stringResource(R.string.naked_pairs_title),
                    onClick = { navigator.navigate(LearnNakedPairsDestination()) }
                )
                LearnRowItem(
                    title = stringResource(R.string.learn_hidden_pairs_title),
                    onClick = { navigator.navigate(LearnHiddenPairsDestination()) }
                )
            }
        }
    }
}