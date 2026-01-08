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

package gr.tonygnk.sudokubeyond.ui.gameshistory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudoku.core.types.GameDifficulty
import gr.tonygnk.sudoku.core.types.GameType
import gr.tonygnk.sudokubeyond.data.database.model.SavedGame
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.domain.repository.SavedGameRepository
import gr.tonygnk.sudokubeyond.ui.util.viewModelBuilder

class HistoryViewModel(
    savedGameRepository: SavedGameRepository,
    appSettingsManager: AppSettingsManager,
) : ViewModel(
) {
    val games = savedGameRepository.getWithBoards()

    var sortType by mutableStateOf(SortType.Descending)
    var sortEntry by mutableStateOf(SortEntry.DateStarted)
    var filterDifficulties by mutableStateOf(emptyList<GameDifficulty>())
    var filterGameTypes by mutableStateOf(emptyList<GameType>())
    var filterByGameState by mutableStateOf(GameStateFilter.All)

    val dateFormat = appSettingsManager.dateFormat

    fun selectFilter(filter: GameDifficulty) {
        filterDifficulties = if (!filterDifficulties.contains(filter)) {
            filterDifficulties + filter
        } else {
            filterDifficulties - filter
        }
    }

    fun selectFilter(filter: GameType) {
        filterGameTypes = if (!filterGameTypes.contains(filter)) {
            filterGameTypes + filter
        } else {
            filterGameTypes - filter
        }
    }

    fun selectFilter(filter: GameStateFilter) {
        this.filterByGameState = filter
    }

    fun switchSortType() {
        sortType = if (sortType == SortType.Ascending) {
            SortType.Descending
        } else {
            SortType.Ascending
        }
    }

    fun selectSortEntry(sortEntry: SortEntry) {
        this.sortEntry = sortEntry
    }

    fun applySortAndFilter(games: List<Pair<SavedGame, SudokuBoard>>): List<Pair<SavedGame, SudokuBoard>> {
        var result = applyFilterDifficulties(games)
        result = applyFilterTypes(result)
        result = applyFilterByGameState(result)
        result = applySort(result, descending = sortType == SortType.Descending)
        return result
    }

    private fun applyFilterDifficulties(games: List<Pair<SavedGame, SudokuBoard>>): List<Pair<SavedGame, SudokuBoard>> {
        return if (filterDifficulties.isNotEmpty()) {
            games.filter {
                filterDifficulties.contains(it.second.difficulty)
            }
        } else {
            games
        }
    }

    private fun applyFilterTypes(games: List<Pair<SavedGame, SudokuBoard>>): List<Pair<SavedGame, SudokuBoard>> {
        return if (filterGameTypes.isNotEmpty()) {
            games.filter {
                filterGameTypes.contains(it.second.type)
            }
        } else {
            games
        }
    }

    private fun applyFilterByGameState(games: List<Pair<SavedGame, SudokuBoard>>): List<Pair<SavedGame, SudokuBoard>> {
        return if (filterByGameState != GameStateFilter.All) {
            games.filter {
                when (filterByGameState) {
                    GameStateFilter.Completed -> !it.first.canContinue
                    GameStateFilter.InProgress -> it.first.canContinue
                    else -> false
                }
            }
        } else {
            games
        }
    }

    private fun applySort(games: List<Pair<SavedGame, SudokuBoard>>, descending: Boolean): List<Pair<SavedGame, SudokuBoard>> {
        return when (sortEntry) {
            SortEntry.GameID -> games.sortedBy(descending = descending) { it.first.uid }
            SortEntry.Timer -> games.sortedBy(descending = descending) { it.first.timer }
            SortEntry.DateStarted -> games.sortedBy(descending = descending) { it.first.startedAt }
            SortEntry.DatePlayed -> games.sortedBy(descending = descending) { it.first.lastPlayed }
        }
    }

    private inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(descending: Boolean = false, crossinline selector: (T) -> R?): List<T> {
        return if (descending) sortedWith(compareByDescending(selector)) else sortedWith(compareBy(selector))
    }

    companion object {
        val builder = viewModelBuilder {
            HistoryViewModel(
                savedGameRepository = LibreSudokuApp.appModule.savedGameRepository,
                appSettingsManager = LibreSudokuApp.appModule.appSettingsManager
            )
        }
    }
}

enum class SortType(val resName: Int) {
    Ascending(R.string.sort_ascending),
    Descending(R.string.sort_descending)
}

enum class SortEntry(val resName: Int) {
    DateStarted(R.string.date_started),
    DatePlayed(R.string.date_last_played),
    Timer(R.string.sort_by_timer),
    GameID(R.string.sort_by_game_id)

}