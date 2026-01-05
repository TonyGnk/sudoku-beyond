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

package com.kaajjo.libresudoku.core.qqwing.advanced_hint

import com.kaajjo.libresudoku.core.Cell

/**
 * Data that [AdvancedHint] returns
 *
 * @property title resource id for title
 * @property textResWithArg resource id and arguments for string resource
 * @property targetCell target cell, the result of hint
 * @property helpCells cells that help understand the hint
 */
data class AdvancedHintData(
    val titleRes: Int,
    val textResWithArg: Pair<Int, List<String>>,
    val targetCell: Cell,
    val helpCells: List<Cell>
)