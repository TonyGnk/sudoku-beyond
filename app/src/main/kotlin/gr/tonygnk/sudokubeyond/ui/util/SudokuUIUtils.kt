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

package gr.tonygnk.sudokubeyond.ui.util

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import gr.tonygnk.sudoku.core.types.GameType

object SudokuUIUtils {
    // factor: 1 - small, 2 - medium, 3 - big
    fun getFontSize(type: GameType, factor: Int): TextUnit {
        return when (type) {
            GameType.Unspecified -> {
                when (factor) {
                    2 -> 26.sp
                    3 -> 34.sp
                    else -> 22.sp
                }
            }

            GameType.Default9x9, GameType.Killer9x9 -> {
                when (factor) {
                    2 -> 28.sp
                    3 -> 36.sp
                    else -> 22.sp
                }
            }

            GameType.Default12x12, GameType.Killer12x12 -> {
                when (factor) {
                    2 -> 24.sp
                    3 -> 32.sp
                    else -> 18.sp
                }
            }

            GameType.Default6x6, GameType.Killer6x6 -> {
                when (factor) {
                    2 -> 34.sp
                    3 -> 40.sp
                    else -> 24.sp
                }
            }
        }
    }
}
