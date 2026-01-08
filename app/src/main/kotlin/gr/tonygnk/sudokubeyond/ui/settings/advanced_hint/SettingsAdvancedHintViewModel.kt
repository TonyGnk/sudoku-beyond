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

package gr.tonygnk.sudokubeyond.ui.settings.advanced_hint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudoku.core.hint.AdvancedHintSettings
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.ui.util.viewModelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsAdvancedHintViewModel(
    private val settingsManager: AppSettingsManager,
) : ViewModel() {
    val advancedHintEnabled = settingsManager.advancedHintEnabled
    val advancedHintSettings = settingsManager.advancedHintSettings

    fun setAdvancedHintEnabled(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.setAdvancedHint(enabled)
        }
    }

    fun updateAdvancedHintSettings(settings: AdvancedHintSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.updateAdvancedHintSettings(settings)
        }
    }

    companion object {
        val builder = viewModelBuilder {
            SettingsAdvancedHintViewModel(
                settingsManager = LibreSudokuApp.appModule.appSettingsManager
            )
        }
    }
}