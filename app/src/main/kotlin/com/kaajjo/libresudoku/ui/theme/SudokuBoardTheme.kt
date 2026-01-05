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

package com.kaajjo.libresudoku.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kaajjo.libresudoku.ui.theme.ColorUtils.blend
import com.kaajjo.libresudoku.ui.theme.ColorUtils.harmonizeWithPrimary

object BoardColors {
    inline val foregroundColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurface.blend(
            MaterialTheme.colorScheme.primary,
            fraction = 0.65f
        )

    inline val notesColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurfaceVariant.blend(
            MaterialTheme.colorScheme.secondary,
            0.4f
        )
    inline val altForegroundColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurfaceVariant.blend(
            MaterialTheme.colorScheme.secondary,
            0.5f
        ).copy(alpha = 0.85f)

    inline val errorColor: Color
        @Composable
        get() = Color(230, 67, 83).harmonizeWithPrimary()

    inline val highlightColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.secondary

    inline val thickLineColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.55f)

    inline val thinLineColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.25f)
}

interface SudokuBoardColors {
    val foregroundColor: Color
    val notesColor: Color
    val altForegroundColor: Color
    val errorColor: Color
    val highlightColor: Color
    val thickLineColor: Color
    val thinLineColor: Color
}

class SudokuBoardColorsImpl(
    override val foregroundColor: Color = Color.White,
    override val notesColor: Color = Color.White,
    override val altForegroundColor: Color = Color.White,
    override val errorColor: Color = Color.White,
    override val highlightColor: Color = Color.White,
    override val thickLineColor: Color = Color.White,
    override val thinLineColor: Color = Color.White,
) : SudokuBoardColors