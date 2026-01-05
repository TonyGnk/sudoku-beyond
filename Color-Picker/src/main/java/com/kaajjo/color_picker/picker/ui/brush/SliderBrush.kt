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
import com.kaajjo.color_picker.picker.ui.gradientColorScaleHSL
import com.kaajjo.color_picker.picker.ui.gradientColorScaleHSV

/*
    HSV Gradients
 */
/**
 * Gradient for Slider for selecting Hue from HSV
 */
fun sliderHueHSVGradient(
    saturation: Float = 1f,
    value: Float = 1f,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = gradientColorScaleHSV(
            saturation = saturation,
            value = value,
            alpha = alpha,
        ),
        start = start,
        end = end
    )
}

fun sliderSaturationHSVGradient(
    hue: Float,
    value: Float = 1f,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
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

fun sliderValueGradient(
    hue: Float,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsv(hue = hue, saturation = 1f, value = 0f, alpha = alpha),
            Color.hsv(hue = hue, saturation = 1f, value = 1f, alpha = alpha)
        ),
        start = start,
        end = end
    )
}

fun sliderAlphaHSVGradient(
    hue: Float = 0f,
    saturation: Float = 1f,
    value: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsv(hue = hue, saturation = saturation, value = value, alpha = 0f),
            Color.hsv(hue = hue, saturation = saturation, value = value, alpha = 1f)
        ),
        start = start,
        end = end
    )
}


/*
    HSL Gradients
 */
/**
 * Gradient for Slider for selecting Hue from HSL
 */
fun sliderHueHSLGradient(
    saturation: Float = 1f,
    lightness: Float = .5f,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = gradientColorScaleHSL(
            saturation = saturation,
            lightness = lightness,
            alpha = alpha,
        ),
        start = start,
        end = end
    )
}

fun sliderSaturationHSLGradient(
    hue: Float,
    lightness: Float = .5f,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
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

fun sliderLightnessGradient(
    hue: Float,
    saturation: Float = 0f,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(hue = hue, saturation = saturation, lightness = 0f, alpha = alpha),
            Color.hsl(hue = hue, saturation = saturation, lightness = .5f, alpha = alpha),
            Color.hsl(hue = hue, saturation = saturation, lightness = 1f, alpha = alpha)
        ),
        start = start,
        end = end
    )
}

fun sliderLightnessGradient3Stops(
    hue: Float,
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(hue = hue, saturation = 1f, lightness = 0f, alpha = alpha),
            Color.hsl(hue = hue, saturation = 1f, lightness = .5f, alpha = alpha),
            Color.hsl(hue = hue, saturation = 1f, lightness = 1f, alpha = alpha)
        ),
        start = start,
        end = end
    )
}

fun sliderAlphaHSLGradient(
    hue: Float,
    saturation: Float = 1f,
    lightness: Float = .5f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(hue = hue, saturation = saturation, lightness = lightness, alpha = 0f),
            Color.hsl(hue = hue, saturation = saturation, lightness = lightness, alpha = 1f)
        ),
        start = start,
        end = end
    )
}

/*
    RGB Gradients
 */
fun sliderRedGradient(
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(hue = 0f, saturation = 1f, lightness = 0f, alpha = alpha),
            Color.hsl(hue = 0f, saturation = 1f, lightness = .5f, alpha = alpha)
        ),
        start = start,
        end = end
    )
}

fun sliderGreenGradient(
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(hue = 120f, saturation = 1f, lightness = 0f, alpha = alpha),
            Color.hsl(hue = 120f, saturation = 1f, lightness = .5f, alpha = alpha)
        ),
        start = start,
        end = end
    )
}

fun sliderBlueGradient(
    alpha: Float = 1f,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(hue = 240f, saturation = 1f, lightness = 0f, alpha = alpha),
            Color.hsl(hue = 240f, saturation = 1f, lightness = .5f, alpha = alpha)
        ),
        start = start,
        end = end
    )
}

fun sliderAlphaRGBGradient(
    red: Float,
    green: Float,
    blue: Float,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite
): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color(red, green, blue, 0f),
            Color(red, green, blue, 1f)
        ),
        start = start,
        end = end
    )
}