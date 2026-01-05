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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsBoardThemeViewModel @Inject constructor(
    private val themeSettingsManager: ThemeSettingsManager,
    private val appSettingsManager: AppSettingsManager
) : ViewModel() {
    val monetSudokuBoard = themeSettingsManager.monetSudokuBoard
    val positionLines = appSettingsManager.positionLines
    val highlightMistakes = appSettingsManager.highlightMistakes
    var crossHighlight = themeSettingsManager.boardCrossHighlight

    fun updateMonetSudokuBoardSetting(enabled: Boolean) {
        viewModelScope.launch {
            themeSettingsManager.setMonetSudokuBoard(enabled)
        }
    }

    fun updatePositionLinesSetting(enabled: Boolean) {
        viewModelScope.launch {
            appSettingsManager.setPositionLines(enabled)
        }
    }

    fun updateBoardCrossHighlight(enabled: Boolean) {
        viewModelScope.launch {
            themeSettingsManager.setBoardCrossHighlight(enabled)
        }
    }

    val fontSize = appSettingsManager.fontSize
    fun updateFontSize(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            appSettingsManager.setFontSize(value)
        }
    }
}