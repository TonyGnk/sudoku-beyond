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

package com.kaajjo.libresudoku.data.database.converters

import androidx.room.TypeConverter
import com.kaajjo.libresudoku.core.qqwing.GameDifficulty

/**
 * Converts Game Difficulty
 */
class GameDifficultyConverter {
    @TypeConverter
    fun fromDifficulty(gameDifficulty: GameDifficulty): Int {
        return when (gameDifficulty) {
            GameDifficulty.Unspecified -> 0
            GameDifficulty.Simple -> 1
            GameDifficulty.Easy -> 2
            GameDifficulty.Moderate -> 3
            GameDifficulty.Hard -> 4
            GameDifficulty.Challenge -> 5
            GameDifficulty.Custom -> 6
        }
    }

    @TypeConverter
    fun toDifficulty(value: Int): GameDifficulty {
        return when (value) {
            0 -> GameDifficulty.Unspecified
            1 -> GameDifficulty.Simple
            2 -> GameDifficulty.Easy
            3 -> GameDifficulty.Moderate
            4 -> GameDifficulty.Hard
            5 -> GameDifficulty.Challenge
            6 -> GameDifficulty.Custom
            else -> GameDifficulty.Unspecified
        }
    }
}