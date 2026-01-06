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

package gr.tonygnk.sudokubeyond.ui.settings.advanced_hint

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.PreferencesConstants
import gr.tonygnk.sudokubeyond.core.qqwing.advanced_hint.AdvancedHintSettings
import gr.tonygnk.sudokubeyond.ui.components.AnimatedNavigation
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRow
import gr.tonygnk.sudokubeyond.ui.components.ScrollbarLazyColumn
import gr.tonygnk.sudokubeyond.ui.gameshistory.ColorfulBadge
import gr.tonygnk.sudokubeyond.ui.settings.SettingsCategory
import gr.tonygnk.sudokubeyond.ui.settings.SettingsScaffoldLazyColumn
import gr.tonygnk.sudokubeyond.ui.theme.ColorUtils.blend
import gr.tonygnk.sudokubeyond.ui.theme.ColorUtils.harmonizeWithPrimary
import gr.tonygnk.sudokubeyond.ui.util.rememberViewModel

@Destination(style = AnimatedNavigation::class)
@Composable
fun SettingsAdvancedHintScreen(
    viewModel: SettingsAdvancedHintViewModel = rememberViewModel(SettingsAdvancedHintViewModel.builder),
    navigator: DestinationsNavigator,
) {
    val advancedHintEnabled by viewModel.advancedHintEnabled.collectAsStateWithLifecycle(
        PreferencesConstants.DEFAULT_ADVANCED_HINT
    )
    val advancedHintSettings by viewModel.advancedHintSettings.collectAsStateWithLifecycle(
        AdvancedHintSettings()
    )
    SettingsScaffoldLazyColumn(
        navigator = navigator,
        titleText = stringResource(R.string.advanced_hint_title)
    ) { paddingValues ->
        ScrollbarLazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                BigCardSwitch(
                    checked = advancedHintEnabled,
                    onClick = { viewModel.setAdvancedHintEnabled(!advancedHintEnabled) }
                )
            }
            item { SettingsCategory(title = stringResource(R.string.settings_advanced_hint_category_techniques)) }
            item {
                TechniqueItem(
                    title = stringResource(R.string.hint_wrong_value_title),
                    checked = advancedHintSettings.checkWrongValue,
                    onClick = {
                        viewModel.updateAdvancedHintSettings(
                            advancedHintSettings.copy(
                                checkWrongValue = !advancedHintSettings.checkWrongValue
                            )
                        )
                    }
                )
            }
            item {
                TechniqueItem(
                    title = stringResource(R.string.hint_full_house_group_title),
                    checked = advancedHintSettings.fullHouse,
                    onClick = {
                        viewModel.updateAdvancedHintSettings(
                            advancedHintSettings.copy(
                                fullHouse = !advancedHintSettings.fullHouse
                            )
                        )
                    }
                )
            }
            item {
                TechniqueItem(
                    title = stringResource(R.string.hint_naked_single_title),
                    checked = advancedHintSettings.nakedSingle,
                    onClick = {
                        viewModel.updateAdvancedHintSettings(
                            advancedHintSettings.copy(
                                nakedSingle = !advancedHintSettings.nakedSingle
                            )
                        )
                    }
                )
            }
            item {
                TechniqueItem(
                    title = stringResource(R.string.hint_hidden_single_title),
                    checked = advancedHintSettings.hiddenSingle,
                    onClick = {
                        viewModel.updateAdvancedHintSettings(
                            advancedHintSettings.copy(
                                hiddenSingle = !advancedHintSettings.hiddenSingle
                            )
                        )
                    }
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(
                            with(MaterialTheme.colorScheme) {
                                primaryContainer
                                    .blend(secondaryContainer)
                                    .copy(alpha = 0.75f)
                                    .compositeOver(surface)
                            }
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    ColorfulBadge(
                        text = stringResource(R.string.label_beta),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        foreground = MaterialTheme.colorScheme.onSecondaryContainer.harmonizeWithPrimary(),
                        background = MaterialTheme.colorScheme.secondaryContainer.harmonizeWithPrimary()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.advanced_hint_in_development),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun BigCardSwitch(
    checked: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = { onClick(!checked) })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.advanced_hint_title),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSecondaryContainer.harmonizeWithPrimary(),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.padding(start = 12.dp))
            Switch(
                checked = checked,
                onCheckedChange = onClick
            )
        }
    }
}

@Composable
fun TechniqueItem(
    title: String,
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferenceRow(
        modifier = modifier,
        title = title,
        onClick = onClick,
        action = {
            Checkbox(
                checked = checked,
                onCheckedChange = { onClick() }
            )
        }
    )
}