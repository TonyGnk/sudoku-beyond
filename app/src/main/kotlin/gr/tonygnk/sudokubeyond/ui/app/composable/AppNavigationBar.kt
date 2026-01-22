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

package gr.tonygnk.sudokubeyond.ui.app.composable

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc

@Composable
fun AppNavigationBar(
    activeChild: MainActivityBloc.PagesConfig,
    onChildSelected: (MainActivityBloc.PagesConfig) -> Unit,
    updateAvailable: Boolean = false,
) {
    val directions = listOf(
        MainActivityBloc.PagesConfig.TopDestination.StatisticsConfig,
        MainActivityBloc.PagesConfig.TopDestination.HomeConfig,
        MainActivityBloc.PagesConfig.TopDestination.MoreConfig,
    )

    val isNavigationBarVisible: Boolean = directions.any { it == activeChild }

    if (isNavigationBarVisible) NavigationBar {
        directions.forEach { destination ->
            NavigationBarItem(
                icon = {
                    if (destination == MainActivityBloc.PagesConfig.TopDestination.MoreConfig
                        && updateAvailable
                    ) {
                        BadgedBox(
                            badge = {
                                Badge()
                            }
                        ) {
                            Icon(
                                painter = painterResource(destination.icon()),
                                contentDescription = null
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(destination.icon()),
                            contentDescription = null
                        )
                    }
                },
                selected = activeChild == destination,
                label = {
                    Text(
                        text = stringResource(destination.label()),
                        fontWeight = FontWeight.Bold
                    )
                },
                onClick = {
                    onChildSelected(destination)
                }
            )
        }
    }
}

private fun MainActivityBloc.PagesConfig.TopDestination.label(): Int {
    return when (this) {
        is MainActivityBloc.PagesConfig.TopDestination.HomeConfig -> R.string.nav_bar_home
        is MainActivityBloc.PagesConfig.TopDestination.StatisticsConfig -> R.string.nav_bar_statistics
        is MainActivityBloc.PagesConfig.TopDestination.MoreConfig -> R.string.nav_bar_more
    }
}

private fun MainActivityBloc.PagesConfig.TopDestination.icon(): Int {
    return when (this) {
        is MainActivityBloc.PagesConfig.TopDestination.HomeConfig -> R.drawable.ic_home
        is MainActivityBloc.PagesConfig.TopDestination.StatisticsConfig -> R.drawable.ic_info
        is MainActivityBloc.PagesConfig.TopDestination.MoreConfig -> R.drawable.ic_more_horiz
    }
}