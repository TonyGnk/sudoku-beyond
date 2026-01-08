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

package gr.tonygnk.sudoku.core.hint

import gr.tonygnk.sudoku.core.model.Cell

/**
 * Data that [AdvancedHint] returns
 *
 * @property titleKey string key for title
 * @property textKeyWithArg string key and arguments for text
 * @property targetCell target cell, the result of hint
 * @property helpCells cells that help understand the hint
 */
data class AdvancedHintData(
    val titleKey: String,
    val textKeyWithArg: Pair<String, List<String>>,
    val targetCell: Cell,
    val helpCells: List<Cell>
)
