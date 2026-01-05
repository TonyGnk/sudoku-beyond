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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/*
    HSV Gradients
 */
/**
 * Gradient for creating HSV saturation change with 0 degree rotation by default.
 */
fun saturationHSVGradient(
    hue: Float,
    value: Float = 1f,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset(Float.POSITIVE_INFINITY, 0f)
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsv(hue = hue, saturation = 0f, value = value, alpha = alpha),
            Color.hsv(hue = hue, saturation = 1f, value = value, alpha = alpha)
        ),
        start = start,
        end = end
    )
}

/**
 * Vertical gradient that goes from 1 value to 0 with 90 degree rotation by default.
 */
fun valueGradient(
    hue: Float,
    alpha: Float = 1f,
    start:Offset = Offset.Zero,
    end:Offset = Offset(0f, Float.POSITIVE_INFINITY)
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsv(hue = hue, saturation = 0f, value = 1f, alpha = alpha),
            Color.hsv(hue = hue, saturation = 0f, value = 0f, alpha = alpha)
        ),
        start = start,
        end = end
    )
}

/*
    HSL Gradients
 */
fun saturationHSLGradient(
    hue: Float,
    lightness: Float = .5f,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset(Float.POSITIVE_INFINITY, 0f)
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(hue = hue, saturation = 0f, lightness = lightness, alpha = alpha),
            Color.hsl(hue = hue, saturation = 1f, lightness = lightness, alpha = alpha),
        ),
        start = start,
        end = end
    )
}

fun lightnessGradient(
    hue: Float,
    alpha: Float = 1f,
    start:Offset = Offset.Zero,
    end:Offset = Offset(0f, Float.POSITIVE_INFINITY)
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(hue = hue, saturation = .5f, lightness = 1f, alpha = alpha),
            Color.hsl(hue = hue, saturation = .5f, lightness = 0f, alpha = alpha)
        ),
        start = start,
        end = end
    )
}