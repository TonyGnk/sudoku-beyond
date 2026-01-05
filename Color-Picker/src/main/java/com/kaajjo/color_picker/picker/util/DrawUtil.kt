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

package com.kaajjo.color_picker.picker.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas


/**
 * Draw 2 rectangles that blend with [BlendMode.Multiply] to draw saturation picker
 */
fun DrawScope.drawBlendingRectGradient(
    dst: Brush,
    dstTopLeft: Offset = Offset.Zero,
    dstSize: Size = this.size,
    src: Brush,
    srcTopLeft: Offset = Offset.Zero,
    srcSize: Size = this.size,
    blendMode: BlendMode = BlendMode.Multiply
) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        drawRect(dst, dstTopLeft, dstSize)
        drawRect(src, srcTopLeft, srcSize, blendMode = blendMode)
        restoreToCount(checkPoint)
    }
}

/**
 * Draw into layer
 */
fun DrawScope.drawIntoLayer(
    content: DrawScope.() -> Unit
) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        content()
        restoreToCount(checkPoint)
    }
}