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

package gr.tonygnk.sudokubeyond.ui.settings.gameplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.ui.util.viewModelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsGameplayViewModel(
    private val settings: AppSettingsManager
) : ViewModel() {
    val inputMethod = settings.inputMethod
    fun updateInputMethod(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settings.setInputMethod(value)
        }
    }

    val mistakesLimit = settings.mistakesLimit
    fun updateMistakesLimit(enabled: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            settings.setMistakesLimit(enabled)
        }

    val timer = settings.timerEnabled
    fun updateTimer(enabled: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            settings.setTimer(enabled)
        }

    val canResetTimer = settings.resetTimerEnabled
    fun updateCanResetTimer(enabled: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            settings.setResetTimer(enabled)
        }

    val disableHints = settings.hintsDisabled
    fun updateHintDisabled(disabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settings.setHintsDisabled(disabled)
        }
    }

    val funKeyboardOverNum = settings.funKeyboardOverNumbers
    fun updateFunKeyboardOverNum(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settings.setFunKeyboardOverNum(enabled)
        }
    }

    companion object {
        val builder = viewModelBuilder {
            SettingsGameplayViewModel(
                settings = LibreSudokuApp.appModule.appSettingsManager
            )
        }
    }
}