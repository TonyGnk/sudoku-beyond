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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudokubeyond.LocalBoardColors
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.core.update.Release
import gr.tonygnk.sudokubeyond.core.update.UpdateUtil
import gr.tonygnk.sudokubeyond.domain.model.MainActivitySettings
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.ui.backup.BackupBloc
import gr.tonygnk.sudokubeyond.ui.backup.BackupScreen
import gr.tonygnk.sudokubeyond.ui.components.ChildStack
import gr.tonygnk.sudokubeyond.ui.components.ChildStackState
import gr.tonygnk.sudokubeyond.ui.create_edit_sudoku.CreateSudokuBloc
import gr.tonygnk.sudokubeyond.ui.create_edit_sudoku.CreateSudokuScreen
import gr.tonygnk.sudokubeyond.ui.explore_folder.ExploreFolderBloc
import gr.tonygnk.sudokubeyond.ui.explore_folder.ExploreFolderScreen
import gr.tonygnk.sudokubeyond.ui.folders.FoldersBloc
import gr.tonygnk.sudokubeyond.ui.folders.FoldersScreen
import gr.tonygnk.sudokubeyond.ui.game.GameBloc
import gr.tonygnk.sudokubeyond.ui.game.GameScreen
import gr.tonygnk.sudokubeyond.ui.gameshistory.GamesHistoryBloc
import gr.tonygnk.sudokubeyond.ui.gameshistory.GamesHistoryScreen
import gr.tonygnk.sudokubeyond.ui.gameshistory.savedgame.SavedGameBloc
import gr.tonygnk.sudokubeyond.ui.gameshistory.savedgame.SavedGameScreen
import gr.tonygnk.sudokubeyond.ui.home.HomeBloc
import gr.tonygnk.sudokubeyond.ui.home.HomeScreen
import gr.tonygnk.sudokubeyond.ui.import_from_file.ImportFromFileBloc
import gr.tonygnk.sudokubeyond.ui.import_from_file.ImportFromFileScreen
import gr.tonygnk.sudokubeyond.ui.learn.LearnBloc
import gr.tonygnk.sudokubeyond.ui.learn.LearnScreen
import gr.tonygnk.sudokubeyond.ui.learn.learnapp.ToolbarTutorialBloc
import gr.tonygnk.sudokubeyond.ui.learn.learnapp.ToolbarTutorialScreen
import gr.tonygnk.sudokubeyond.ui.learn.learnsudoku.LearnBasic
import gr.tonygnk.sudokubeyond.ui.learn.learnsudoku.LearnBasicBloc
import gr.tonygnk.sudokubeyond.ui.learn.learnsudoku.LearnHiddenPairs
import gr.tonygnk.sudokubeyond.ui.learn.learnsudoku.LearnHiddenPairsBloc
import gr.tonygnk.sudokubeyond.ui.learn.learnsudoku.LearnNakedPairs
import gr.tonygnk.sudokubeyond.ui.learn.learnsudoku.LearnNakedPairsBloc
import gr.tonygnk.sudokubeyond.ui.learn.learnsudoku.LearnSudokuRules
import gr.tonygnk.sudokubeyond.ui.learn.learnsudoku.LearnSudokuRulesBloc
import gr.tonygnk.sudokubeyond.ui.more.MoreBloc
import gr.tonygnk.sudokubeyond.ui.more.MoreScreen
import gr.tonygnk.sudokubeyond.ui.more.about.AboutBloc
import gr.tonygnk.sudokubeyond.ui.more.about.AboutLibrariesBloc
import gr.tonygnk.sudokubeyond.ui.more.about.AboutLibrariesScreen
import gr.tonygnk.sudokubeyond.ui.more.about.AboutScreen
import gr.tonygnk.sudokubeyond.ui.onboarding.WelcomeBloc
import gr.tonygnk.sudokubeyond.ui.onboarding.WelcomeScreen
import gr.tonygnk.sudokubeyond.ui.settings.SettingsCategoriesBloc
import gr.tonygnk.sudokubeyond.ui.settings.SettingsCategoriesScreen
import gr.tonygnk.sudokubeyond.ui.settings.advanced_hint.SettingsAdvancedHintBloc
import gr.tonygnk.sudokubeyond.ui.settings.advanced_hint.SettingsAdvancedHintScreen
import gr.tonygnk.sudokubeyond.ui.settings.appearance.SettingsAppearanceBloc
import gr.tonygnk.sudokubeyond.ui.settings.appearance.SettingsAppearanceScreen
import gr.tonygnk.sudokubeyond.ui.settings.assistance.SettingsAssistanceBloc
import gr.tonygnk.sudokubeyond.ui.settings.assistance.SettingsAssistanceScreen
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.AutoUpdateBloc
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.AutoUpdateScreen
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.UpdateChannel
import gr.tonygnk.sudokubeyond.ui.settings.boardtheme.SettingsBoardTheme
import gr.tonygnk.sudokubeyond.ui.settings.boardtheme.SettingsBoardThemeBloc
import gr.tonygnk.sudokubeyond.ui.settings.gameplay.SettingsGameplayBloc
import gr.tonygnk.sudokubeyond.ui.settings.gameplay.SettingsGameplayScreen
import gr.tonygnk.sudokubeyond.ui.settings.language.SettingsLanguageBloc
import gr.tonygnk.sudokubeyond.ui.settings.language.SettingsLanguageScreen
import gr.tonygnk.sudokubeyond.ui.settings.other.SettingsOtherBloc
import gr.tonygnk.sudokubeyond.ui.settings.other.SettingsOtherScreen
import gr.tonygnk.sudokubeyond.ui.statistics.StatisticsBloc
import gr.tonygnk.sudokubeyond.ui.statistics.StatisticsScreen
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
        stackState = ChildStackState(
            blocContext = blocContext,
            stack = stack,
            onChildSelect = bloc::onChildSelected,
            backHandler = blocContext.backHandler,
            onBackClick = bloc::onBackClicked,
        ),
    )
}

@Composable
private fun MainActivityContent(
    settings: MainActivitySettings,
    stackState: ChildStackState<MainActivityBloc.PagesConfig, MainActivityBloc.PagesBloc>,
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

//        LaunchedEffect(settings.firstLaunch) {
//            if (settings.firstLaunch) {
//                navController.navigate(
//                    route = WelcomeScreenDestination.route,
//                    navOptions = navOptions {
//                        popUpTo(HomeScreenDestination.route) {
//                            inclusive = true
//                        }
//                    }
//                )
//            }
//        }

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
                        activeChild = stackState.stack.active.configuration,
                        onChildSelected = stackState.onChildSelect,
                        updateAvailable = latestRelease != null && latestRelease!!.name.toString() != settings.updateDismissedName
                    )
                },
                contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            ) { paddingValuesOuter ->
                stackState.ChildStack(
                    modifier = Modifier.padding(paddingValuesOuter),
                ) { child ->
                    when (val bloc = child.instance) {
                        is HomeBloc -> HomeScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                        )
                        is MoreBloc -> MoreScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                        )
                        is StatisticsBloc -> StatisticsScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                        )
                        is GamesHistoryBloc -> GamesHistoryScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )
                        is SavedGameBloc -> SavedGameScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )
                        is ExploreFolderBloc -> ExploreFolderScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )
                        is GameBloc -> GameScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )
                        is FoldersBloc -> FoldersScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )
                        is ImportFromFileBloc -> ImportFromFileScreen(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )
                        is WelcomeBloc -> WelcomeScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                            removeWelcomeAndNavigateToHome = {
                                stackState.onBackClick()
                                stackState.onChildSelect(MainActivityBloc.PagesConfig.TopDestination.HomeConfig)
                            }
                        )
                        is AboutBloc -> AboutScreen(
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )
                        is AutoUpdateBloc -> AutoUpdateScreen(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )

                        LearnBloc -> LearnScreen(
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )

                        LearnSudokuRulesBloc -> LearnSudokuRules(
                            finish = stackState.onBackClick
                        )

                        LearnBasicBloc -> LearnBasic(
                            finish = stackState.onBackClick
                        )

                        LearnNakedPairsBloc -> LearnNakedPairs(
                            finish = stackState.onBackClick
                        )

                        LearnHiddenPairsBloc -> LearnHiddenPairs(
                            finish = stackState.onBackClick
                        )

                        ToolbarTutorialBloc -> ToolbarTutorialScreen(
                            finish = stackState.onBackClick
                        )

                        is BackupBloc -> BackupScreen(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )

                        is SettingsCategoriesBloc -> SettingsCategoriesScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )

                        is SettingsAdvancedHintBloc -> SettingsAdvancedHintScreen(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )

                        is SettingsAppearanceBloc -> SettingsAppearanceScreen(
                            bloc = bloc,
                            navigate = stackState.onChildSelect,
                            finish = stackState.onBackClick
                        )

                        is SettingsAssistanceBloc -> SettingsAssistanceScreen(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )

                        is SettingsGameplayBloc -> SettingsGameplayScreen(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )

                        SettingsLanguageBloc -> SettingsLanguageScreen(
                            finish = stackState.onBackClick
                        )

                        is SettingsBoardThemeBloc -> SettingsBoardTheme(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )

                        is SettingsOtherBloc -> SettingsOtherScreen(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )

                        AboutLibrariesBloc -> AboutLibrariesScreen(
                            finish = stackState.onBackClick
                        )

                        is CreateSudokuBloc -> CreateSudokuScreen(
                            bloc = bloc,
                            finish = stackState.onBackClick
                        )
                    }
                }
            }
        }
    }
}