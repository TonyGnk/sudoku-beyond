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

package gr.tonygnk.sudokubeyond.ui.theme.icons

import androidx.compose.material.icons.Icons
import gr.tonygnk.sudokubeyond.ui.util.iconFromXmlPath

val Icons.Filled.Bitcoin by lazy {
    iconFromXmlPath(
        pathStr = "M11.5 11.5v-2.5c1.75 0 2.789.25 2.789 1.25 0 1.172-1.684 1.25-2.789 1.25zm0 .997v2.503c1.984 0 3.344-.188 3.344-1.258 0-1.148-1.469-1.245-3.344-1.245zm12.5-.497c0 6.627-5.373 12-12 12s-12-5.373-12-12 5.373-12 12-12 12 5.373 12 12zm-7 1.592c0-1.279-1.039-1.834-1.789-2.025.617-.223 1.336-1.138 1.046-2.228-.245-.922-1.099-1.74-3.257-1.813v-1.526h-1v1.5h-.5v-1.5h-1v1.5h-2.5v1.5h.817c.441 0 .683.286.683.702v4.444c0 .429-.253.854-.695.854h-.539l-.25 1.489h2.484v1.511h1v-1.511h.5v1.511h1v-1.5c2.656 0 4-1.167 4-2.908z",
        viewportHeight = 24f,
        viewportWidth = 24f
    )
}