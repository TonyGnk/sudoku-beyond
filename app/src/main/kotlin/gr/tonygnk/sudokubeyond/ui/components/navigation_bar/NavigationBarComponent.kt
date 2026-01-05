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

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import gr.tonygnk.sudokubeyond.NavGraphs
import gr.tonygnk.sudokubeyond.appCurrentDestinationAsState
import gr.tonygnk.sudokubeyond.destinations.MoreScreenDestination
import gr.tonygnk.sudokubeyond.startAppDestination
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@Composable
fun NavigationBarComponent(
    navController: NavController,
    isVisible: Boolean,
    updateAvailable: Boolean = false,
) {
    val directions = listOf(
        NavigationBarDestination.Statistics,
        NavigationBarDestination.Home,
        NavigationBarDestination.More
    )

    val currentDestination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    if (isVisible) {
        NavigationBar {
            directions.forEach { destination ->
                NavigationBarItem(
                    icon = {
                        if (destination.direction.route == MoreScreenDestination.route
                            && updateAvailable
                        ) {
                            BadgedBox(
                                badge = {
                                    Badge()
                                }
                            ) {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = null
                                )
                            }
                        } else {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = null
                            )
                        }
                    },
                    selected = currentDestination == destination.direction,
                    label = {
                        Text(
                            text = stringResource(destination.label),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    onClick = {
                        navController.toDestinationsNavigator().navigate(destination.direction) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}