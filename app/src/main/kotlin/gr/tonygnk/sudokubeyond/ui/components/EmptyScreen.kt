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

package gr.tonygnk.sudokubeyond.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gr.tonygnk.sudokubeyond.ui.theme.LibreSudokuTheme
import gr.tonygnk.sudokubeyond.ui.util.LightDarkPreview

@Composable
fun EmptyScreen(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = { }
) {
    val emptyFace by remember {
        mutableStateOf(
            listOf(
                "Σ(ಠ_ಠ)",
                "(･Д･。",
                "(っ˘̩╭╮˘̩)っ",
                "ಥ_ಥ"
            ).random()
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emptyFace,
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = text,
            textAlign = TextAlign.Center
        )
        content()
    }
}

@LightDarkPreview
@Composable
private fun EmptyScreenPreview() {
    LibreSudokuTheme {
        Surface {
            EmptyScreen("There is so empty...")
        }
    }
}