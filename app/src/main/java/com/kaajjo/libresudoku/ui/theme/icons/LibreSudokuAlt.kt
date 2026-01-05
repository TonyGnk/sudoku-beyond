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

package com.kaajjo.libresudoku.ui.theme.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import com.kaajjo.libresudoku.ui.util.iconFromXmlPath

val Icons.Rounded.LibreSudokuIconAlt by lazy {
    iconFromXmlPath(
        pathStr = """
                M 40 0 h 1.9 v 60 h -1.9 z M 20 0 h 1.9 v 60 h -1.9 z M 0 20 l 0 -2 l 60 0 l 0 2 z M 0 40 l 0 -2 l 60 0 l 0 2 z
            """.trimIndent(),
        viewportWidth = 60f,
        viewportHeight = 60f,
        fillColor = Color(0xFFC0C0C0.toInt())
    )
}