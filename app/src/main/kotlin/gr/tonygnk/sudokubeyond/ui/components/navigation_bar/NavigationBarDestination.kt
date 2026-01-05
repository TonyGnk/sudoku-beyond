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

package gr.tonygnk.sudokubeyond.ui.components.navigation_bar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.destinations.HomeScreenDestination
import gr.tonygnk.sudokubeyond.destinations.MoreScreenDestination
import gr.tonygnk.sudokubeyond.destinations.StatisticsScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

sealed class NavigationBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    data object Home : NavigationBarDestination(
        HomeScreenDestination,
        Icons.Rounded.Home,
        R.string.nav_bar_home
    )

    data object Statistics : NavigationBarDestination(
        StatisticsScreenDestination,
        Icons.Rounded.Info,
        R.string.nav_bar_statistics
    )

    data object More : NavigationBarDestination(
        MoreScreenDestination,
        Icons.Rounded.MoreHoriz,
        R.string.nav_bar_more
    )
}