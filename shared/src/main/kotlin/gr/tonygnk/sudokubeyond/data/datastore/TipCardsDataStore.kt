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

package gr.tonygnk.sudokubeyond.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class TipCardsDataStore(context: Context) {
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = "tip_card")
    private val dataStore = context.createDataStore

    private val recordCardKey = booleanPreferencesKey("record")
    private val streakCardKey = booleanPreferencesKey("streak")

    suspend fun setRecordCard(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[recordCardKey] = enabled
        }
    }

    val recordCard = dataStore.data.map { preferences ->
        preferences[recordCardKey] ?: true
    }

    suspend fun setStreakCard(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[streakCardKey] = enabled
        }
    }

    val streakCard = dataStore.data.map { preferences ->
        preferences[streakCardKey] ?: true
    }
}