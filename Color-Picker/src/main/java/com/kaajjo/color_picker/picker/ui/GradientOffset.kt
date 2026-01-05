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

package com.kaajjo.color_picker.picker.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush


/**
 *
 * Get a [GradientOffset] that rotate a gradient clockwise with specified angle in degrees.
 * Default value for [GradientOffset] is [GradientAngle.CW0] which is 0 degrees
 * that returns a horizontal gradient.
 *
 * Get start and end offsets that are limited between [0f, Float.POSITIVE_INFINITY] in x and
 * y axes wrapped in [GradientOffset].
 * Infinity is converted to Composable width on x axis, height on y axis in shader.
 *
 * Default angle for [Brush.linearGradient] when no offset is 0 degrees in Compose ,
 * [Brush.verticalGradient]  is [Brush.linearGradient] with 90 degrees.
 *
 * ```
 *  0 degrees
 *  start = Offset(0f,0f),
 *  end = Offset(Float.POSITIVE_INFINITY,0f)
 *
 * 45 degrees
 * start = Offset(0f, Float.POSITIVE_INFINITY),
 * end = Offset(Float.POSITIVE_INFINITY, 0f)
 *
 * 90 degrees
 * start = Offset(0f, Float.POSITIVE_INFINITY),
 * end = Offset.Zero
 *
 * 135 degrees
 * start = Offset.Infinity,
 * end = Offset.Zero
 *
 * 180 degrees
 * start = Offset(Float.POSITIVE_INFINITY, 0f),
 * end = Offset.Zero,
 *
 * ```
 */
fun GradientOffset(angle: GradientAngle = GradientAngle.CW0): GradientOffset {
    return when (angle) {
        GradientAngle.CW45 -> GradientOffset(
            start = Offset.Zero,
            end = Offset.Infinite
        )
        GradientAngle.CW90 -> GradientOffset(
            start = Offset.Zero,
            end = Offset(0f, Float.POSITIVE_INFINITY)
        )
        GradientAngle.CW135 -> GradientOffset(
            start = Offset(Float.POSITIVE_INFINITY, 0f),
            end = Offset(0f, Float.POSITIVE_INFINITY)
        )
        GradientAngle.CW180 -> GradientOffset(
            start = Offset(Float.POSITIVE_INFINITY, 0f),
            end = Offset.Zero,
        )
        GradientAngle.CW225 -> GradientOffset(
            start = Offset.Infinite,
            end = Offset.Zero
        )
        GradientAngle.CW270 -> GradientOffset(
            start = Offset(0f, Float.POSITIVE_INFINITY),
            end = Offset.Zero
        )
        GradientAngle.CW315 -> GradientOffset(
            start = Offset(0f, Float.POSITIVE_INFINITY),
            end = Offset(Float.POSITIVE_INFINITY, 0f)
        )
        else -> GradientOffset(
            start = Offset.Zero,
            end = Offset(Float.POSITIVE_INFINITY, 0f)
        )
    }
}

/**
 * Offset for [Brush.linearGradient] to rotate gradient depending on [start] and [end] offsets.
 */
data class GradientOffset(val start: Offset, val end: Offset)

enum class GradientAngle {
    CW0, CW45, CW90, CW135, CW180, CW225, CW270, CW315
}