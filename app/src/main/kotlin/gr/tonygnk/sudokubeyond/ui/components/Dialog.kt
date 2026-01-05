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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.ui.util.isScrolledToEnd
import gr.tonygnk.sudokubeyond.ui.util.isScrolledToStart

@Composable
fun <T> SelectionDialog(
    selectedValue: T,
    title: String,
    onDismiss: () -> Unit,
    entries: Map<T, String>,
    icon: @Composable (() -> Unit)? = null,
    onSelect: (T) -> Unit
) {
    AlertDialog(
        icon = icon,
        title = { Text(text = title) },
        text = {
            Box {
                val lazyListState = rememberLazyListState()

                if (!lazyListState.isScrolledToStart()) HorizontalDivider(Modifier.align(Alignment.TopCenter))
                if (!lazyListState.isScrolledToEnd()) HorizontalDivider(Modifier.align(Alignment.BottomCenter))

                ScrollbarLazyColumn(state = lazyListState) {
                    entries.forEach { item ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.small)
                                    .clickable { onSelect(item.key) },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = item.key == selectedValue,
                                    onClick = { onSelect(item.key) }
                                )
                                Text(
                                    text = item.value,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}