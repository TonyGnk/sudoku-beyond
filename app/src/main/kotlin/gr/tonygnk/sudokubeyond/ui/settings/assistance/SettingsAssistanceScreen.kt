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

package gr.tonygnk.sudokubeyond.ui.settings.assistance

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Adjust
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.LooksOne
import androidx.compose.material.icons.outlined.Pin
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
fun SettingsAssistanceScreen(
    bloc: SettingsAssistanceBloc,
    finish: () -> Unit,
) {
    var mistakesDialog by rememberSaveable { mutableStateOf(false) }

    val highlightMistakes by bloc.highlightMistakes.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES
    )
    val autoEraseNotes by bloc.autoEraseNotes.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_AUTO_ERASE_NOTES)
    val highlightIdentical by bloc.highlightIdentical.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_HIGHLIGHT_IDENTICAL
    )
    val remainingUse by bloc.remainingUse.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_REMAINING_USES)

    SettingsScaffoldLazyColumn(
        titleText = stringResource(R.string.pref_assistance),
        finish = finish
    ) { paddingValues ->
        ScrollbarLazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_mistakes_check),
                    subtitle = when (highlightMistakes) {
                        0 -> stringResource(R.string.pref_mistakes_check_off)
                        1 -> stringResource(R.string.pref_mistakes_check_violations)
                        2 -> stringResource(R.string.pref_mistakes_check_final)
                        else -> stringResource(R.string.pref_mistakes_check_off)
                    },
                    onClick = { mistakesDialog = true },
                    painter = rememberVectorPainter(Icons.Outlined.Adjust)
                )
            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_highlight_identical),
                    subtitle = stringResource(R.string.pref_highlight_identical_summ),
                    checked = highlightIdentical,
                    onClick = {
                        bloc.updateHighlightIdentical(!highlightIdentical)
                    },
                    painter = rememberVectorPainter(Icons.Outlined.LooksOne)
                )
            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_remaining_uses),
                    subtitle = stringResource(R.string.pref_remaining_uses_summ),
                    checked = remainingUse,
                    onClick = { bloc.updateRemainingUse(!remainingUse) },
                    painter = rememberVectorPainter(Icons.Outlined.Pin)
                )

            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_auto_erase_notes),
                    checked = autoEraseNotes,
                    onClick = { bloc.updateAutoEraseNotes(!autoEraseNotes) },
                    painter = rememberVectorPainter(Icons.Outlined.AutoFixHigh)
                )
            }
        }

        if (mistakesDialog) {
            SelectionDialog(
                title = stringResource(R.string.pref_mistakes_check),
                selections = listOf(
                    stringResource(R.string.pref_mistakes_check_off),
                    stringResource(R.string.pref_mistakes_check_violations),
                    stringResource(R.string.pref_mistakes_check_final)
                ),
                selected = highlightMistakes,
                onSelect = { index ->
                    bloc.updateMistakesHighlight(index)
                },
                onDismiss = { mistakesDialog = false }
            )
        }
    }
}