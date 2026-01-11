/*
 *  Copyright (C) 2026 TonyGnk
 *
 *  This file is part of Sudoku Beyond.
 *  Originally from LibreSudoku (https://github.com/kaajjo/LibreSudoku)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */

package gr.tonygnk.sudokubeyond.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.UpdateChannel

@Immutable
data class MainActivitySettings(
    val dynamicColors: Boolean,
    val darkTheme: Int,
    val amoledBlack: Boolean,
    val firstLaunch: Boolean,
    val monetSudokuBoard: Boolean,
    val colorSeed: Color,
    val paletteStyle: PaletteStyle,
    val autoUpdateChannel: UpdateChannel,
    val updateDismissedName: String,
)