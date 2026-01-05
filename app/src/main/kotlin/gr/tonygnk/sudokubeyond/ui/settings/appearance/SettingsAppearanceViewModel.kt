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

package gr.tonygnk.sudokubeyond.ui.settings.appearance

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SettingsAppearanceViewModel @Inject constructor(
    private val themeSettings: ThemeSettingsManager,
    private val settings: AppSettingsManager
) : ViewModel() {
    val darkTheme by lazy {
        themeSettings.darkTheme
    }

    fun updateDarkTheme(value: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            themeSettings.setDarkTheme(value)
        }

    val dynamicColors by lazy {
        themeSettings.dynamicColors
    }

    fun updateDynamicColors(enabled: Boolean) =
        viewModelScope.launch {
            themeSettings.setDynamicColors(enabled)
        }

    val amoledBlack by lazy {
        themeSettings.amoledBlack
    }

    fun updateAmoledBlack(enabled: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            themeSettings.setAmoledBlack(enabled)
        }

    val dateFormat = settings.dateFormat
    fun updateDateFormat(format: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settings.setDateFormat(format)
        }
    }

    val paletteStyle by lazy { themeSettings.themePaletteStyle }
    fun updatePaletteStyle(index: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            themeSettings.setPaletteStyle(ThemeSettingsManager.paletteStyles[index].first)
        }

    val seedColor by lazy { themeSettings.themeColorSeed }
    fun updateCurrentSeedColor(seedColor: Color) {
        viewModelScope.launch(Dispatchers.IO) {
            themeSettings.setCurrentThemeColor(seedColor)
        }
    }

    fun checkCustomDateFormat(pattern: String): Boolean {
        return try {
            DateTimeFormatter.ofPattern(pattern)
            true
        } catch (e: Exception) {
            false
        }
    }
}