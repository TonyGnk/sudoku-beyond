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

package gr.tonygnk.sudokubeyond.ui.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.qqwing.GameDifficulty
import gr.tonygnk.sudokubeyond.core.qqwing.GameType
import gr.tonygnk.sudokubeyond.data.database.model.Record
import gr.tonygnk.sudokubeyond.data.database.model.SavedGame
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.TipCardsDataStore
import gr.tonygnk.sudokubeyond.domain.repository.RecordRepository
import gr.tonygnk.sudokubeyond.domain.repository.SavedGameRepository
import gr.tonygnk.sudokubeyond.ui.util.viewModelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val recordRepository: RecordRepository,
    private val tipCardsDataStore: TipCardsDataStore,
    savedGameRepository: SavedGameRepository,
    appSettingsManager: AppSettingsManager,
) : ViewModel() {
    var showDeleteDialog by mutableStateOf(false)
    var selectedDifficulty by mutableStateOf(GameDifficulty.Unspecified)
    var selectedType by mutableStateOf(GameType.Unspecified)

    val recordTipCard = tipCardsDataStore.recordCard
    val streakTipCard = tipCardsDataStore.streakCard

    var recordList: Flow<List<Record>> = recordRepository.getAllSortByTime()
    val savedGamesList: Flow<List<SavedGame>> = savedGameRepository.getAll()

    val dateFormat = appSettingsManager.dateFormat

    fun deleteRecord(recordEntity: Record) {
        viewModelScope.launch {
            recordRepository.delete(recordEntity)
        }
    }

    fun setDifficulty(difficulty: GameDifficulty) {
        selectedDifficulty = difficulty

        if (difficulty != GameDifficulty.Unspecified && selectedType == GameType.Unspecified) {
            selectedType = GameType.Default9x9
        } else if (difficulty == GameDifficulty.Unspecified) {
            selectedType = GameType.Unspecified
        }

        loadRecords(selectedType == GameType.Unspecified && selectedDifficulty == GameDifficulty.Unspecified)
    }

    fun setType(type: GameType) {
        selectedType = type

        if (selectedType == GameType.Unspecified) {
            selectedDifficulty = GameDifficulty.Unspecified
        } else if (selectedType != GameType.Unspecified && selectedDifficulty == GameDifficulty.Unspecified) {
            selectedDifficulty = GameDifficulty.Easy
        }

        loadRecords(selectedType == GameType.Unspecified && selectedDifficulty == GameDifficulty.Unspecified)
    }

    private fun loadRecords(all: Boolean) {
        recordList = if (all) {
            recordRepository.getAllSortByTime()
        } else {
            recordRepository.getAll(selectedDifficulty, selectedType)
        }
    }

    fun setRecordTipCard(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tipCardsDataStore.setRecordCard(enabled)
        }
    }

    fun setStreakTipCard(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tipCardsDataStore.setStreakCard(enabled)
        }
    }

    fun getCurrentStreak(games: List<SavedGame>): Int {
        var currentStreak = 0
        games.forEach { game ->
            if (game.completed) {
                currentStreak = if (!game.giveUp && !game.canContinue) currentStreak + 1 else 0
            }
        }
        return currentStreak
    }

    fun getMaxStreak(games: List<SavedGame>): Int {
        var maxStreak = 0
        var currentStreak = 0
        games.forEach { game ->
            if (game.completed && !game.canContinue) {
                currentStreak = if (!game.giveUp) currentStreak + 1 else 0
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak
                }
            }
        }
        return maxStreak
    }

    fun getWinRate(savedGames: List<SavedGame>): Float {
        return savedGames
            .count { it.completed && !it.giveUp && !it.canContinue } * 100f / savedGames.count()
            .toFloat()
    }

    companion object {
        val builder = viewModelBuilder {
            StatisticsViewModel(
                recordRepository = LibreSudokuApp.appModule.recordRepository,
                tipCardsDataStore = LibreSudokuApp.appModule.tipCardsDataStore,
                savedGameRepository = LibreSudokuApp.appModule.savedGameRepository,
                appSettingsManager = LibreSudokuApp.appModule.appSettingsManager
            )
        }
    }
}