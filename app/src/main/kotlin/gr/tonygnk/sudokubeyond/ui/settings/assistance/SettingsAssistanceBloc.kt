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

package gr.tonygnk.sudokubeyond.ui.settings.assistance

import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalDecomposeApi::class)
class SettingsAssistanceBloc(
    blocContext: BlocContext,
    private val settings: AppSettingsManager,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {

    private val scope = lifecycle.coroutineScope

    val remainingUse = settings.remainingUse
    fun updateRemainingUse(enabled: Boolean) {
        scope.launch(Dispatchers.IO) {
            settings.setRemainingUse(enabled)
        }
    }

    val highlightIdentical = settings.highlightIdentical
    fun updateHighlightIdentical(enabled: Boolean) =
        scope.launch(Dispatchers.IO) {
            settings.setSameValuesHighlight(enabled)
        }

    val autoEraseNotes = settings.autoEraseNotes
    fun updateAutoEraseNotes(enabled: Boolean) {
        scope.launch(Dispatchers.IO) {
            settings.setAutoEraseNotes(enabled)
        }
    }

    val highlightMistakes = settings.highlightMistakes
    fun updateMistakesHighlight(index: Int) {
        scope.launch(Dispatchers.IO) {
            settings.setHighlightMistakes(index)
        }
    }

    companion object Companion {
        operator fun invoke(blocContext: BlocContext) = SettingsAssistanceBloc(
            blocContext = blocContext,
            settings = LibreSudokuApp.appModule.appSettingsManager
        )
    }
}