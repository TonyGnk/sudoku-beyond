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

package gr.tonygnk.sudokubeyond.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.ui.theme.ColorUtils.harmonizeWithPrimary
import gr.tonygnk.sudokubeyond.ui.theme.LibreSudokuTheme
import gr.tonygnk.sudokubeyond.ui.util.LightDarkPreview

@Composable
fun HelpCard(
    modifier: Modifier = Modifier,
    title: String,
    details: String,
    painter: Painter?,
    onCloseClicked: () -> Unit,
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (painter != null) {
                        Icon(
                            painter = painter,
                            contentDescription = null
                        )
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                IconButton(onClick = onCloseClicked) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null
                    )
                }
            }
            Text(
                text = details,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@LightDarkPreview
@Composable
fun HelpCardPreview() {
    LibreSudokuTheme {
        HelpCard(
            title = "This is the title",
            details = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tempus arcu vitae elit congue scelerisque. Sed a vestibulum tellus. Suspendisse tristique dui eget nisi dictum tempus",
            painter = painterResource(R.drawable.ic_verified),
            onCloseClicked = {}
        )
    }
}

@Composable
fun GrantPermissionCard(
    modifier: Modifier = Modifier,
    title: String,
    details: String,
    @DrawableRes drawableRes: Int?,
    confirmButton: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (drawableRes != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.harmonizeWithPrimary())
                        ) {
                            Icon(
                                painter = painterResource(drawableRes),
                                contentDescription = null,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Text(
                text = details,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            confirmButton()
        }
    }
}

@LightDarkPreview
@Composable
fun GrantPermissionCardPreview() {
    LibreSudokuTheme {
        GrantPermissionCard(
            title = "This is the title",
            details = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tempus arcu vitae elit congue scelerisque. Sed a vestibulum tellus. Suspendisse tristique dui eget nisi dictum tempus",
            drawableRes = R.drawable.ic_tips_and_updates,
            confirmButton = {
                Button(
                    onClick = { },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Grant")
                }
            }
        )
    }
}