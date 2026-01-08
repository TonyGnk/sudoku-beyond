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
import gr.tonygnk.sudoku.core.hint.AdvancedHintData

/**
 * Extension property to map AdvancedHintData string keys to Android resource IDs
 */
val AdvancedHintData.titleRes: Int
    get() = when (this.titleKey) {
        "hint_wrong_value_title" -> R.string.hint_wrong_value_title
        "hint_naked_single_title" -> R.string.hint_naked_single_title
        "hint_full_house_group_title" -> R.string.hint_full_house_group_title
        "hint_hidden_single_title" -> R.string.hint_hidden_single_title
        else -> 0
    }

val AdvancedHintData.textResWithArg: Pair<Int, List<String>>
    get() {
        val resId = when (this.textKeyWithArg.first) {
            "hint_wrong_value_detail" -> R.string.hint_wron_value_detail
            "hint_naked_single_detail" -> R.string.hint_naked_single_detail
            "hint_full_house_group_detail" -> R.string.hint_full_house_group_detail
            "hint_hidden_single_detail" -> R.string.hint_hidden_single_detail
            else -> 0
        }
        return Pair(resId, this.textKeyWithArg.second)
    }