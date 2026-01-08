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

package gr.tonygnk.sudoku.core.types

enum class GameType(
    val size: Int,
    val sectionHeight: Int,
    val sectionWidth: Int,
    val displayKey: String
) {
    Unspecified(1, 1, 1, "type_unspecified"),
    Default9x9(9, 3, 3, "type_default_9x9"),
    Default12x12(12, 3, 4, "type_default_12x12"),
    Default6x6(6, 2, 3, "type_default_6x6"),
    Killer9x9(9, 3, 3, "type_killer_9x9"),
    Killer12x12(12, 3, 4, "type_killer_12x12"),
    Killer6x6(6, 2, 3, "type_killer_6x6"),
}
