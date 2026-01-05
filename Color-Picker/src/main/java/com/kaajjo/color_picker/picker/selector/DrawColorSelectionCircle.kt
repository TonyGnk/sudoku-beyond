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

package com.kaajjo.color_picker.picker.selector

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Draw selection circle with white and black colors
 * Stroke draws half in and half out of the current radius.
 * With 200 radius 20 stroke width starts from 190 and ends at 210.
 * @param center center of the selection circle
 * @param radius radius of the selection circle
 */
fun DrawScope.drawHueSelectionCircle(
    center: Offset,
    radius: Float
) {
    drawCircle(
        Color.White,
        radius = radius,
        center = center,
        style = Stroke(width = radius / 4)
    )

    drawCircle(
        Color.Black,
        radius = radius + radius / 8,
        center = center,
        style = Stroke(width = radius / 8)
    )
}