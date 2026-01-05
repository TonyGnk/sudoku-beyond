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

package com.kaajjo.libresudoku.ui.components.board

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawPositionLines(
    row: Int,
    col: Int,
    gameSize: Int,
    cellSize: Float,
    lineLength: Float,
    color: Color,
    cornerRadius: CornerRadius
) {
    drawPositionLineVertical(
        col = col,
        gameSize = gameSize,
        cellSize = cellSize,
        lineLength = lineLength,
        color = color,
        cornerRadius = cornerRadius
    )
    drawPositionLineHorizontal(
        row = row,
        gameSize = gameSize,
        cellSize = cellSize,
        lineLength = lineLength,
        color = color,
        cornerRadius = cornerRadius
    )
}

private fun DrawScope.drawPositionLineVertical(
    col: Int,
    gameSize: Int,
    cellSize: Float,
    lineLength: Float,
    color: Color,
    cornerRadius: CornerRadius,
) {
    val topLeft = if (col == 0) cornerRadius else CornerRadius.Zero
    val topRight = if (col == gameSize - 1) cornerRadius else CornerRadius.Zero
    val bottomLeft = if (col == 0) cornerRadius else CornerRadius.Zero
    val bottomRight = if (col == gameSize - 1) cornerRadius else CornerRadius.Zero

    drawPath(
        path = Path().apply {
            addRoundRect(
                roundRect = RoundRect(
                    rect = Rect(
                        offset = Offset(
                            x = col * cellSize,
                            y = 0f
                        ),
                        size = Size(cellSize, lineLength)
                    ),
                    topLeft = topLeft,
                    topRight = topRight,
                    bottomLeft = bottomLeft,
                    bottomRight = bottomRight,
                )
            )
        },
        color = color
    )
}

private fun DrawScope.drawPositionLineHorizontal(
    row: Int,
    gameSize: Int,
    cellSize: Float,
    lineLength: Float,
    color: Color,
    cornerRadius: CornerRadius,
) {
    val topLeft = if (row == 0) cornerRadius else CornerRadius.Zero
    val topRight = if (row == 0) cornerRadius else CornerRadius.Zero
    val bottomLeft = if (row == gameSize - 1) cornerRadius else CornerRadius.Zero
    val bottomRight = if (row == gameSize - 1) cornerRadius else CornerRadius.Zero

    drawPath(
        path = Path().apply {
            addRoundRect(
                roundRect = RoundRect(
                    rect = Rect(
                        offset = Offset(
                            x = 0f,
                            y = row * cellSize
                        ),
                        size = Size(lineLength, cellSize)
                    ),
                    topLeft = topLeft,
                    topRight = topRight,
                    bottomLeft = bottomLeft,
                    bottomRight = bottomRight,
                )
            )
        },
        color = color
    )
}