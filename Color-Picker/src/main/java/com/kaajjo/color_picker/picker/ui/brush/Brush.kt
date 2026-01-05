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

package com.kaajjo.color_picker.picker.ui.brush

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import com.kaajjo.color_picker.picker.ui.WhiteTransparent


/*
    Vertical Brushes for adding lightness or value to Rectangle Color pickers
 */
fun transparentToBlackVerticalGradient(
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY
): Brush {
    return Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
        startY = startY,
        endY = endY
    )
}

fun transparentToWhiteVerticalGradient(
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY
): Brush {
    return Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.White),
        startY = startY,
        endY = endY
    )
}

fun transparentToGrayVerticalGradient(
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY
): Brush {
    return Brush.verticalGradient(
        colors = listOf(Color.Transparent, Gray),
        startY = startY,
        endY = endY
    )
}

fun whiteToTransparentToBlackVerticalGradient(
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY
): Brush {
    return Brush.verticalGradient(
        0.0f to Color.White,
        0.5f to WhiteTransparent,
        0.5f to Color.Transparent,
        1f to Color.Black,
        startY = startY,
        endY = endY
    )
}

fun whiteToTransparentToBlackHorizontalGradient(
    startX: Float = 0.0f,
    endX: Float = Float.POSITIVE_INFINITY
): Brush {
    return Brush.horizontalGradient(
        0.0f to Color.White,
        0.5f to WhiteTransparent,
        0.5f to Color.Transparent,
        1f to Color.Black,
        startX = startX,
        endX = endX
    )
}

fun whiteToBlackGradient(
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY
): Brush {
    return Brush.verticalGradient(
        colors = listOf(Color.White, Color.Black),
        startY = startY,
        endY = endY
    )
}