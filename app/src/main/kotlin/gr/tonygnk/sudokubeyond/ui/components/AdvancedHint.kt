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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.blend
import com.materialkolor.ktx.harmonize
import gr.tonygnk.sudoku.core.hint.AdvancedHintData
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.extensions.textResWithArg
import gr.tonygnk.sudokubeyond.extensions.titleRes

@androidx.compose.runtime.Composable
fun AdvancedHintContainer(
    advancedHintData: AdvancedHintData,
    onApplyClick: (() -> Unit)?,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                advancedHintData.let {
                    BackHandler {
                        onBackClick()
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .background(
                                with(MaterialTheme.colorScheme) {
                                    primary.blend(secondaryContainer, 0.75f)
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_auto_fix),
                                contentDescription = null,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                tint = with(MaterialTheme.colorScheme) {
                                    onSecondaryContainer.harmonize(primary)
                                }
                            )
                            Text(
                                text = stringResource(it.titleRes),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = with(MaterialTheme.colorScheme) {
                                    onSecondaryContainer.harmonize(primary)
                                }
                            )
                        }
                        IconButton(
                            onClick = onSettingsClick,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_sliders),
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        text = stringResource(
                            it.textResWithArg.first,
                            *it.textResWithArg.second.toTypedArray()
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onBackClick,
            ) {
                Text(stringResource(R.string.nav_back))
            }
            FilledTonalButton(
                onClick = onApplyClick ?: { },
                enabled = onApplyClick != null
            ) {
                Text(stringResource(R.string.action_apply))
            }
        }
    }
}