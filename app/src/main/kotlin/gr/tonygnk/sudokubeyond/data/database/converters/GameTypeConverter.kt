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

package gr.tonygnk.sudokubeyond.data.database.converters

import androidx.room.TypeConverter
import gr.tonygnk.sudokubeyond.core.qqwing.GameType

/**
 * Converts GameType
 */
class GameTypeConverter {
    @TypeConverter
    fun fromType(gameType: GameType): Int {
        return when (gameType) {
            GameType.Unspecified -> 0
            GameType.Default9x9 -> 1
            GameType.Default12x12 -> 2
            GameType.Default6x6 -> 3
            GameType.Killer9x9 -> 4
            GameType.Killer12x12 -> 5
            GameType.Killer6x6 -> 6
        }
    }

    @TypeConverter
    fun toType(value: Int): GameType {
        return when (value) {
            0 -> GameType.Unspecified
            1 -> GameType.Default9x9
            2 -> GameType.Default12x12
            3 -> GameType.Default6x6
            4 -> GameType.Killer9x9
            5 -> GameType.Killer12x12
            6 -> GameType.Killer6x6
            else -> GameType.Unspecified
        }
    }
}