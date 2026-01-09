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

@file:OptIn(ExperimentalDecomposeApi::class)

package gr.tonygnk.sudokubeyond.ui.app.composable

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV2
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.essenty.backhandler.BackHandler
import com.ramcosta.composedestinations.DestinationsNavHost
import gr.tonygnk.sudokubeyond.LocalBoardColors
import gr.tonygnk.sudokubeyond.NavGraphs
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.core.update.Release
import gr.tonygnk.sudokubeyond.core.update.UpdateUtil
import gr.tonygnk.sudokubeyond.destinations.HomeScreenDestination
import gr.tonygnk.sudokubeyond.destinations.MoreScreenDestination
import gr.tonygnk.sudokubeyond.destinations.StatisticsScreenDestination
import gr.tonygnk.sudokubeyond.destinations.WelcomeScreenDestination
import gr.tonygnk.sudokubeyond.domain.model.MainActivitySettings
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.ui.components.navigation_bar.NavigationBarComponent
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.UpdateChannel
import gr.tonygnk.sudokubeyond.ui.theme.BoardColors
import gr.tonygnk.sudokubeyond.ui.theme.LibreSudokuTheme
import gr.tonygnk.sudokubeyond.ui.theme.SudokuBoardColorsImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun MainActivityScreen(blocContext: BlocContext) {
    val bloc = remember { MainActivityBloc(blocContext) }

    val settings by bloc.settings.collectAsStateWithLifecycle()
    val stack by bloc.stack.collectAsStateWithLifecycle()

    MainActivityContent(
        settings = settings,
        stack = stack,
        onChildSelected = bloc::onChildSelected,
        backHandler = blocContext.backHandler,
        onBackClicked = bloc::onBackClicked,
    )
}


@Composable
private fun MainActivityContent(
    settings: MainActivitySettings,
    stack: ChildStack<MainActivityBloc.PagesConfig, MainActivityBloc.PagesBloc>,
    onChildSelected: (MainActivityBloc.PagesConfig) -> Unit,
    backHandler: BackHandler,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkMode = when (settings.darkTheme) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }

    LibreSudokuTheme(
        darkTheme = isDarkMode,
        dynamicColor = settings.dynamicColors,
        amoled = settings.amoledBlack,
        colorSeed = settings.colorSeed,
        paletteStyle = settings.paletteStyle
    ) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        var bottomBarState by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(navBackStackEntry) {
            bottomBarState = when (navBackStackEntry?.destination?.route) {
                StatisticsScreenDestination.route, HomeScreenDestination.route, MoreScreenDestination.route -> true
                else -> false
            }
        }
        LaunchedEffect(settings.firstLaunch) {
            if (settings.firstLaunch) {
                navController.navigate(
                    route = WelcomeScreenDestination.route,
                    navOptions = navOptions {
                        popUpTo(HomeScreenDestination.route) {
                            inclusive = true
                        }
                    }
                )
            }
        }

        val boardColors =
            if (settings.monetSudokuBoard) {
                SudokuBoardColorsImpl(
                    foregroundColor = BoardColors.foregroundColor,
                    notesColor = BoardColors.notesColor,
                    altForegroundColor = BoardColors.altForegroundColor,
                    errorColor = BoardColors.errorColor,
                    highlightColor = BoardColors.highlightColor,
                    thickLineColor = BoardColors.thickLineColor,
                    thinLineColor = BoardColors.thinLineColor
                )
            } else {
                SudokuBoardColorsImpl(
                    foregroundColor = MaterialTheme.colorScheme.onSurface,
                    notesColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    altForegroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    errorColor = BoardColors.errorColor,
                    highlightColor = MaterialTheme.colorScheme.outline,
                    thickLineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.55f),
                    thinLineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.25f)
                )
            }
        var latestRelease by remember { mutableStateOf<Release?>(null) }
        if (settings.autoUpdateChannel != UpdateChannel.Disabled) {
            LaunchedEffect(Unit) {
                if (latestRelease == null) {
                    withContext(Dispatchers.IO) {
                        try {
                            latestRelease = UpdateUtil.checkForUpdate(settings.autoUpdateChannel == UpdateChannel.Beta)
                        } catch (e: Exception) {
                            Log.e("UpdateUtil", "Failed to check for update: ${e.message.toString()}")
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        CompositionLocalProvider(LocalBoardColors provides boardColors) {
            Scaffold(
                modifier = modifier,
                bottomBar = {
                    AppNavigationBar(
                        activeChild = stack.active.configuration,
                        onChildSelected = onChildSelected,
                        updateAvailable = latestRelease != null && latestRelease!!.name.toString() != settings.updateDismissedName
                    )
                },
                contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            ) { paddingValuesOuter ->
                ChildStack(
                    stack = stack,
                    modifier = Modifier.padding(paddingValuesOuter),
                    animation = stackAnimation(
                        animator = fade(),
                        predictiveBackParams = {
                            PredictiveBackParams(
                                backHandler = backHandler,
                                onBack = onBackClicked,
                                animatable = { backEvent -> androidPredictiveBackAnimatableV2(backEvent) },
                            )
                        },
                    )
                ) { child ->
                    when (child.configuration) {
                        MainActivityBloc.PagesConfig.HomeConfig -> Text("Home Screen Placeholder")
                        MainActivityBloc.PagesConfig.MoreConfig -> Text("More Screen Placeholder")
                        MainActivityBloc.PagesConfig.StatisticsConfig -> Text("Statistics Screen Placeholder")
                        MainActivityBloc.PagesConfig.OldNavigationGraph -> {
                            Scaffold(
                                modifier = modifier,
                                bottomBar = {
                                    NavigationBarComponent(
                                        navController = navController,
                                        isVisible = bottomBarState,
                                        updateAvailable = latestRelease != null && latestRelease!!.name.toString() != settings.updateDismissedName
                                    )
                                },
                                contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
                            ) { paddingValues ->
                                DestinationsNavHost(
                                    navGraph = NavGraphs.root,
                                    navController = navController,
                                    startRoute = NavGraphs.root.startRoute,
                                    modifier = Modifier.padding(paddingValues)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}