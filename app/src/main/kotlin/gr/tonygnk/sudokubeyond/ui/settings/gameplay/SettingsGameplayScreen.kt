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

package gr.tonygnk.sudokubeyond.ui.settings.gameplay

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SwitchAccessShortcut
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.PreferencesConstants
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRow
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRowSwitch
import gr.tonygnk.sudokubeyond.ui.components.ScrollbarLazyColumn
import gr.tonygnk.sudokubeyond.ui.settings.SelectionDialog
import gr.tonygnk.sudokubeyond.ui.settings.SettingsScaffoldLazyColumn

@Composable
fun SettingsGameplayScreen(
    bloc: SettingsGameplayBloc,
    finish: () -> Unit,
) {
    var inputMethodDialog by rememberSaveable { mutableStateOf(false) }

    val inputMethod by bloc.inputMethod.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_INPUT_METHOD)
    val mistakesLimit by bloc.mistakesLimit.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_MISTAKES_LIMIT)
    val hintDisabled by bloc.disableHints.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_HINTS_DISABLED)
    val timerEnabled by bloc.timer.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_SHOW_TIMER)
    val resetTimer by bloc.canResetTimer.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_GAME_RESET_TIMER)
    val funKeyboardOverNum by bloc.funKeyboardOverNum.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_FUN_KEYBOARD_OVER_NUM
    )

    SettingsScaffoldLazyColumn(
        titleText = stringResource(R.string.pref_gameplay),
        finish = finish
    ) { paddingValues ->
        ScrollbarLazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_input),
                    subtitle = when (inputMethod) {
                        0 -> stringResource(R.string.pref_input_cell_first)
                        1 -> stringResource(R.string.pref_input_digit_first)
                        else -> ""
                    },
                    onClick = { inputMethodDialog = true },
                    painter = rememberVectorPainter(Icons.Outlined.EditNote)
                )
            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_mistakes_limit),
                    subtitle = stringResource(R.string.pref_mistakes_limit_summ),
                    checked = mistakesLimit,
                    onClick = { bloc.updateMistakesLimit(!mistakesLimit) },
                    painter = rememberVectorPainter(Icons.Outlined.Block)
                )
            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_disable_hints),
                    subtitle = stringResource(R.string.pref_disable_hints_summ),
                    checked = hintDisabled,
                    onClick = { bloc.updateHintDisabled(!hintDisabled) },
                    painter = rememberVectorPainter(Icons.Outlined.VisibilityOff)
                )
            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_show_timer),
                    checked = timerEnabled,
                    onClick = { bloc.updateTimer(!timerEnabled) },
                    painter = rememberVectorPainter(Icons.Outlined.Schedule)
                )
            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_reset_timer),
                    checked = resetTimer,
                    onClick = { bloc.updateCanResetTimer(!resetTimer) },
                    painter = rememberVectorPainter(Icons.Outlined.HistoryToggleOff)
                )
            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_fun_keyboard_over_num),
                    subtitle = stringResource(R.string.pref_fun_keyboard_over_num_subtitle),
                    checked = funKeyboardOverNum,
                    onClick = {
                        bloc.updateFunKeyboardOverNum(!funKeyboardOverNum)
                    },
                    painter = rememberVectorPainter(Icons.Outlined.SwitchAccessShortcut)
                )
            }
        }

        if (inputMethodDialog) {
            SelectionDialog(
                title = stringResource(R.string.pref_input),
                selections = listOf(
                    stringResource(R.string.pref_input_cell_first),
                    stringResource(R.string.pref_input_digit_first)
                ),
                selected = inputMethod,
                onSelect = { index ->
                    bloc.updateInputMethod(index)
                },
                onDismiss = { inputMethodDialog = false }
            )
        }
    }
}