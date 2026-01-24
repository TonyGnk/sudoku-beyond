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
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalDecomposeApi::class)
class SettingsAppearanceBloc(
    blocContext: BlocContext,
    private val themeSettings: ThemeSettingsManager,
    private val settings: AppSettingsManager,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {

    private val scope = lifecycle.coroutineScope

    val darkTheme by lazy {
        themeSettings.darkTheme
    }

    fun updateDarkTheme(value: Int) =
        scope.launch(Dispatchers.IO) {
            themeSettings.setDarkTheme(value)
        }

    val dynamicColors by lazy {
        themeSettings.dynamicColors
    }

    fun updateDynamicColors(enabled: Boolean) =
        scope.launch {
            themeSettings.setDynamicColors(enabled)
        }

    val amoledBlack by lazy {
        themeSettings.amoledBlack
    }

    fun updateAmoledBlack(enabled: Boolean) =
        scope.launch(Dispatchers.IO) {
            themeSettings.setAmoledBlack(enabled)
        }

    val dateFormat = settings.dateFormat
    fun updateDateFormat(format: String) {
        scope.launch(Dispatchers.IO) {
            settings.setDateFormat(format)
        }
    }

    val paletteStyle by lazy { themeSettings.themePaletteStyle }
    fun updatePaletteStyle(index: Int) =
        scope.launch(Dispatchers.IO) {
            themeSettings.setPaletteStyle(ThemeSettingsManager.paletteStyles[index].first)
        }

    val seedColor by lazy { themeSettings.themeColorSeed }
    fun updateCurrentSeedColor(seedColor: Color) {
        scope.launch(Dispatchers.IO) {
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

    companion object Companion {
        operator fun invoke(blocContext: BlocContext) = SettingsAppearanceBloc(
            blocContext = blocContext,
            themeSettings = LibreSudokuApp.appModule.themeSettingsManager,
            settings = LibreSudokuApp.appModule.appSettingsManager
        )
    }
}