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

package com.kaajjo.libresudoku.ui.settings.assistance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaajjo.libresudoku.data.datastore.AppSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsAssistanceViewModel @Inject constructor(
    private val settings: AppSettingsManager,
) : ViewModel() {
    val remainingUse = settings.remainingUse
    fun updateRemainingUse(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settings.setRemainingUse(enabled)
        }
    }

    val highlightIdentical = settings.highlightIdentical
    fun updateHighlightIdentical(enabled: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            settings.setSameValuesHighlight(enabled)
        }

    val autoEraseNotes = settings.autoEraseNotes
    fun updateAutoEraseNotes(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settings.setAutoEraseNotes(enabled)
        }
    }

    val highlightMistakes = settings.highlightMistakes
    fun updateMistakesHighlight(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settings.setHighlightMistakes(index)
        }
    }
}