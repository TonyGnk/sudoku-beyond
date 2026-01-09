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

package gr.tonygnk.sudokubeyond

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.arkivanov.decompose.defaultComponentContext
import com.materialkolor.PaletteStyle
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.core.PreferencesConstants
import gr.tonygnk.sudokubeyond.core.update.Release
import gr.tonygnk.sudokubeyond.core.update.UpdateUtil
import gr.tonygnk.sudokubeyond.core.utils.GlobalExceptionHandler
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.destinations.HomeScreenDestination
import gr.tonygnk.sudokubeyond.destinations.ImportFromFileScreenDestination
import gr.tonygnk.sudokubeyond.destinations.MoreScreenDestination
import gr.tonygnk.sudokubeyond.destinations.StatisticsScreenDestination
import gr.tonygnk.sudokubeyond.destinations.WelcomeScreenDestination
import gr.tonygnk.sudokubeyond.ui.app_crash.CrashActivity
import gr.tonygnk.sudokubeyond.ui.components.navigation_bar.NavigationBarComponent
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.UpdateChannel
import gr.tonygnk.sudokubeyond.ui.theme.BoardColors
import gr.tonygnk.sudokubeyond.ui.theme.LibreSudokuTheme
import gr.tonygnk.sudokubeyond.ui.theme.SudokuBoardColorsImpl
import gr.tonygnk.sudokubeyond.ui.util.findActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val LocalBoardColors = staticCompositionLocalOf { SudokuBoardColorsImpl() }

class MainActivity : AppCompatActivity() {
    lateinit var settings: AppSettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (!BuildConfig.DEBUG) {
            GlobalExceptionHandler.initialize(applicationContext, CrashActivity::class.java)
        }

        val mainActivityBloc = MainActivityBloc(defaultComponentContext())

        setContent {
            val dynamicColors by mainActivityBloc.dc.collectAsStateWithLifecycle(isSystemInDarkTheme())
            val darkTheme by mainActivityBloc.darkTheme.collectAsStateWithLifecycle(PreferencesConstants.DEFAULT_DARK_THEME)
            val amoledBlack by mainActivityBloc.amoledBlack.collectAsStateWithLifecycle(PreferencesConstants.DEFAULT_AMOLED_BLACK)
            val firstLaunch by mainActivityBloc.firstLaunch.collectAsStateWithLifecycle(false)
            val colorSeed by mainActivityBloc.colorSeed.collectAsStateWithLifecycle(initialValue = Color.Red)
            val paletteStyle by mainActivityBloc.paletteStyle.collectAsStateWithLifecycle(initialValue = PaletteStyle.TonalSpot)
            val autoUpdateChannel by mainActivityBloc.autoUpdateChannel.collectAsStateWithLifecycle(UpdateChannel.Disabled)
            val updateDismissedName by mainActivityBloc.updateDismissedName.collectAsStateWithLifecycle("")

            val isDarkMode = when (darkTheme) {
                1 -> false
                2 -> true
                else -> isSystemInDarkTheme()
            }

            LibreSudokuTheme(
                darkTheme = isDarkMode,
                dynamicColor = dynamicColors,
                amoled = amoledBlack,
                colorSeed = colorSeed,
                paletteStyle = paletteStyle
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
                LaunchedEffect(firstLaunch) {
                    if (firstLaunch) {
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

                val monetSudokuBoard by mainActivityBloc.monetSudokuBoard.collectAsStateWithLifecycle(
                    initialValue = PreferencesConstants.DEFAULT_MONET_SUDOKU_BOARD
                )

                val boardColors =
                    if (monetSudokuBoard) {
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
                if (autoUpdateChannel != UpdateChannel.Disabled) {
                    LaunchedEffect(Unit) {
                        if (latestRelease == null) {
                            withContext(Dispatchers.IO) {
                                try {
                                    latestRelease = UpdateUtil.checkForUpdate(autoUpdateChannel == UpdateChannel.Beta)
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
                        bottomBar = {
                            NavigationBarComponent(
                                navController = navController,
                                isVisible = bottomBarState,
                                updateAvailable = latestRelease != null && latestRelease!!.name.toString() != updateDismissedName
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

class MainActivityBloc(
    blocContext: BlocContext,
    themeSettingsManager: ThemeSettingsManager,
    appSettingsManager: AppSettingsManager,
) : BlocContext by blocContext {
    val dc = themeSettingsManager.dynamicColors
    val darkTheme = themeSettingsManager.darkTheme
    val amoledBlack = themeSettingsManager.amoledBlack
    val firstLaunch = appSettingsManager.firstLaunch
    val monetSudokuBoard = themeSettingsManager.monetSudokuBoard
    val colorSeed = themeSettingsManager.themeColorSeed
    val paletteStyle = themeSettingsManager.themePaletteStyle
    val autoUpdateChannel = appSettingsManager.autoUpdateChannel
    val updateDismissedName = appSettingsManager.updateDismissedName

    companion object {
        operator fun invoke(blocContext: BlocContext) = MainActivityBloc(
            blocContext = blocContext,
            themeSettingsManager = LibreSudokuApp.appModule.themeSettingsManager,
            appSettingsManager = LibreSudokuApp.appModule.appSettingsManager
        )
    }
}

@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "content://",
            mimeType = "*/*",
            action = Intent.ACTION_VIEW
        )
    ]
)
@Composable
fun HandleImportFromFileDeepLink(
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val activity = context.findActivity()
        if (activity != null) {
            val intentData = activity.intent.data
            if (intentData != null) {
                navigator.navigate(
                    ImportFromFileScreenDestination(
                        fileUri = intentData.toString(),
                        fromDeepLink = true
                    )
                )
            }
        }
    }
}