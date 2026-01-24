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

package gr.tonygnk.sudokubeyond.ui.settings.boardtheme

import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalDecomposeApi::class)
class SettingsBoardThemeBloc(
    blocContext: BlocContext,
    private val themeSettingsManager: ThemeSettingsManager,
    private val appSettingsManager: AppSettingsManager,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {

    private val scope = lifecycle.coroutineScope

    val monetSudokuBoard = themeSettingsManager.monetSudokuBoard
    val positionLines = appSettingsManager.positionLines
    val highlightMistakes = appSettingsManager.highlightMistakes
    var crossHighlight = themeSettingsManager.boardCrossHighlight

    fun updateMonetSudokuBoardSetting(enabled: Boolean) {
        scope.launch {
            themeSettingsManager.setMonetSudokuBoard(enabled)
        }
    }

    fun updatePositionLinesSetting(enabled: Boolean) {
        scope.launch {
            appSettingsManager.setPositionLines(enabled)
        }
    }

    fun updateBoardCrossHighlight(enabled: Boolean) {
        scope.launch {
            themeSettingsManager.setBoardCrossHighlight(enabled)
        }
    }

    val fontSize = appSettingsManager.fontSize
    fun updateFontSize(value: Int) {
        scope.launch(Dispatchers.IO) {
            appSettingsManager.setFontSize(value)
        }
    }

    companion object Companion {
        operator fun invoke(blocContext: BlocContext) = SettingsBoardThemeBloc(
            blocContext = blocContext,
            themeSettingsManager = LibreSudokuApp.appModule.themeSettingsManager,
            appSettingsManager = LibreSudokuApp.appModule.appSettingsManager,
        )
    }
}