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
import com.kaajjo.libresudoku.ui.util.iconFromXmlPath

val Icons.Rounded.Ton by lazy {
    iconFromXmlPath(
        pathStr = "M24.56 0.628H5.439C1.923 0.628 -0.306 4.42 1.463 7.486L13.264 27.941C14.035 29.277 15.964 29.277 16.735 27.941L28.538 7.486C30.304 4.425 28.076 0.628 24.563 0.628H24.56ZM13.255 21.807L10.685 16.833L4.483 5.741C4.074 5.032 4.579 4.122 5.436 4.122H13.252V21.809L13.255 21.807ZM25.511 5.739L19.312 16.835L16.742 21.807V4.119H24.558C25.415 4.119 25.92 5.029 25.511 5.739Z",
        viewportWidth = 30f,
        viewportHeight = 30f
    )
}