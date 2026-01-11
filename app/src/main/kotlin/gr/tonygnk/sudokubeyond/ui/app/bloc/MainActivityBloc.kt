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

package gr.tonygnk.sudokubeyond.ui.app.bloc

import androidx.compose.runtime.Stable
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.materialkolor.PaletteStyle
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.domain.model.MainActivitySettings
import gr.tonygnk.sudokubeyond.domain.usecase.app.GetMainActivitySettingsUseCase
import gr.tonygnk.sudokubeyond.ui.gameshistory.GamesHistoryBloc
import gr.tonygnk.sudokubeyond.ui.gameshistory.savedgame.SavedGameBloc
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.UpdateChannel
import gr.tonygnk.sudokubeyond.ui.statistics.StatisticsBloc
import gr.tonygnk.sudokubeyond.ui.util.toStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable

@OptIn(ExperimentalDecomposeApi::class)
class MainActivityBloc(
    blocContext: BlocContext,
    getMainActivitySettingsUseCase: GetMainActivitySettingsUseCase,
) : BlocContext by blocContext {
    private val scope = lifecycle.coroutineScope

    val settings: StateFlow<MainActivitySettings> = getMainActivitySettingsUseCase()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainActivitySettings(
                dynamicColors = false,
                darkTheme = 0,
                amoledBlack = false,
                firstLaunch = false,
                monetSudokuBoard = false,
                colorSeed = androidx.compose.ui.graphics.Color.Red,
                paletteStyle = PaletteStyle.TonalSpot,
                autoUpdateChannel = UpdateChannel.Disabled,
                updateDismissedName = "",
            )
        )

    private val navigation = StackNavigation<PagesConfig>()

    val stack: StateFlow<ChildStack<PagesConfig, PagesBloc>> = childStack(
        source = navigation,
        serializer = PagesConfig.serializer(),
        initialConfiguration = PagesConfig.TopDestination.HomeConfig,
    ) { config, blocContext ->
        when (config) {
            PagesConfig.TopDestination.HomeConfig -> TemporaryHomeBloc(blocContext = blocContext)
            PagesConfig.TopDestination.MoreConfig -> TemporaryMoreBloc(blocContext = blocContext)
            PagesConfig.TopDestination.StatisticsConfig -> StatisticsBloc(blocContext = blocContext)
            PagesConfig.GamesHistoryConfig -> GamesHistoryBloc(blocContext = blocContext)
            is PagesConfig.SavedGameConfig -> SavedGameBloc(blocContext, config.boardUid)
            PagesConfig.TopDestination.OldNavigationGraph -> TemporaryOldNavigation(blocContext = blocContext)
        }
    }.toStateFlow()

    fun onChildSelected(newConfig: PagesConfig) {
        if (newConfig == PagesConfig.TopDestination.HomeConfig) {
            navigation.replaceAll(newConfig)
        } else {
            navigation.pushToFront(newConfig)
        }
    }

    fun onBackClicked() {
        navigation.pop()
    }

    @Stable
    interface PagesBloc

    @Serializable
    @Stable
    sealed interface PagesConfig {
        @Serializable
        sealed interface TopDestination : PagesConfig {
            @Serializable
            data object StatisticsConfig : TopDestination

            @Serializable
            data object HomeConfig : TopDestination

            @Serializable
            data object MoreConfig : TopDestination

            @Serializable
            data object OldNavigationGraph : TopDestination
        }

        @Serializable
        data object GamesHistoryConfig : PagesConfig

        @Serializable
        data class SavedGameConfig(val boardUid: Long) : PagesConfig
    }

    companion object {
        operator fun invoke(blocContext: BlocContext) = MainActivityBloc(
            blocContext = blocContext,
            getMainActivitySettingsUseCase = GetMainActivitySettingsUseCase(
                themeSettingsManager = LibreSudokuApp.appModule.themeSettingsManager,
                appSettingsManager = LibreSudokuApp.appModule.appSettingsManager
            )
        )
    }
}

class TemporaryMoreBloc(
    blocContext: BlocContext,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext

class TemporaryHomeBloc(
    blocContext: BlocContext,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext

class TemporaryOldNavigation(
    blocContext: BlocContext,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext