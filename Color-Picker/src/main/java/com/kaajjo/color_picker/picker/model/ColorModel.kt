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

package com.kaajjo.color_picker.picker.model

/**
 * Color Model HSV(Hue-Saturation-Value), HSL(Hue-Saturation-Lightness), RGB(Red-Green-Blue)
 */
enum class ColorModel {
    RGB, HSV, HSL
}

/**
 * Color Modes that contain HSL, HSV, RGB, and Gradient
 */
enum class ColorMode {
    HSL, HSV, RGB, Gradient
}