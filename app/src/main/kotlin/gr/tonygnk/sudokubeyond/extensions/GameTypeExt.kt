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
import gr.tonygnk.sudoku.core.types.GameType

/**
 * Extension property to map GameType to Android string resource ID
 */
val GameType.resName: Int
    get() = when (this.displayKey) {
        "type_default_9x9" -> R.string.type_default_9x9
        "type_default_12x12" -> R.string.type_default_12x12
        "type_default_6x6" -> R.string.type_default_6x6
        "type_killer_9x9" -> R.string.type_killer_9x9
        "type_killer_12x12" -> R.string.type_killer_12x12
        "type_killer_6x6" -> R.string.type_killer_6x6
        else -> R.string.type_unspecified
    }