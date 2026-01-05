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

package gr.tonygnk.sudokubeyond.ui.settings.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudokubeyond.data.database.AppDatabase
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.TipCardsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsOtherViewModel @Inject constructor(
    private val settings: AppSettingsManager,
    private val tipCardsDataStore: TipCardsDataStore,
    private val appDatabase: AppDatabase
) : ViewModel() {
    val saveLastSelectedDifficultyType = settings.saveSelectedGameDifficultyType
    fun updateSaveLastSelectedDifficultyType(enabled: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            settings.setSaveSelectedGameDifficultyType(enabled)
        }

    val keepScreenOn = settings.keepScreenOn
    fun updateKeepScreenOn(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settings.setKeepScreenOn(enabled)
        }
    }

    fun resetTipCards() {
        viewModelScope.launch {
            tipCardsDataStore.setStreakCard(true)
            tipCardsDataStore.setRecordCard(true)
        }
    }

    fun deleteAllTables() {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.clearAllTables()
        }
    }
}