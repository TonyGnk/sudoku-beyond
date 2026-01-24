/*
 * Copyright (C) 2026 TonyGnk
 *
 * This file is part of Sudoku Beyond.
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

package gr.tonygnk.sudokubeyond.extensions

import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudoku.core.types.GameDifficulty

/**
 * Extension property to map GameDifficulty to Android string resource ID
 */
val GameDifficulty.resName: Int
    get() = when (this.displayKey) {
        "difficulty_easy" -> R.string.difficulty_easy
        "difficulty_moderate" -> R.string.difficulty_moderate
        "difficulty_hard" -> R.string.difficulty_hard
        "difficulty_challenge" -> R.string.difficulty_challenge
        "difficulty_custom" -> R.string.difficulty_custom
        else -> R.string.difficulty_unspecified
    }