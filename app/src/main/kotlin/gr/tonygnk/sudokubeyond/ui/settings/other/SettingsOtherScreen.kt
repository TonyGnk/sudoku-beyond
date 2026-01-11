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

package gr.tonygnk.sudokubeyond.ui.settings.other

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.PreferencesConstants
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRow
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRowSwitch
import gr.tonygnk.sudokubeyond.ui.components.ScrollbarLazyColumn
import gr.tonygnk.sudokubeyond.ui.settings.SettingsScaffoldLazyColumn
import kotlinx.coroutines.launch

@Composable
fun SettingsOtherScreen(
    bloc: SettingsOtherBloc,
    finish: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var resetGameDataDialog by rememberSaveable { mutableStateOf(false) }

    val saveLastSelectedDifficultyType by bloc.saveLastSelectedDifficultyType
        .collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_SAVE_LAST_SELECTED_DIFF_TYPE)
    val keepScreenOn by bloc.keepScreenOn.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_KEEP_SCREEN_ON)

    SettingsScaffoldLazyColumn(
        titleText = stringResource(R.string.pref_other),
        finish = finish,
        snackbarHostState = snackbarHostState
    ) { paddingValues ->
        ScrollbarLazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_save_last_diff_and_type),
                    subtitle = stringResource(R.string.pref_save_last_diff_and_type_subtitle),
                    checked = saveLastSelectedDifficultyType,
                    onClick = {
                        bloc.updateSaveLastSelectedDifficultyType(!saveLastSelectedDifficultyType)
                    },
                    painter = rememberVectorPainter(Icons.Outlined.Bookmark)
                )
            }

            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_keep_screen_on),
                    checked = keepScreenOn,
                    onClick = {
                        bloc.updateKeepScreenOn(!keepScreenOn)
                    },
                    painter = rememberVectorPainter(Icons.Outlined.Smartphone)
                )
            }

            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_reset_tipcards),
                    onClick = {
                        bloc.resetTipCards()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                context.resources.getString(R.string.pref_tipcards_reset)
                            )
                        }
                    },
                    painter = rememberVectorPainter(Icons.Outlined.Clear)
                )
            }

            if (!bloc.launchedFromGame) {
                item {
                    PreferenceRow(
                        title = stringResource(R.string.pref_delete_stats),
                        onClick = {
                            resetGameDataDialog = true
                        },
                        painter = rememberVectorPainter(Icons.Outlined.Delete)
                    )
                }
            }
        }

        if (resetGameDataDialog) {
            AlertDialog(
                title = { Text(stringResource(R.string.pref_delete_stats)) },
                text = { Text(stringResource(R.string.pref_delete_stats_summ)) },
                confirmButton = {
                    TextButton(onClick = {
                        bloc.deleteAllTables()
                        resetGameDataDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                context.resources.getString(R.string.action_deleted)
                            )
                        }
                    }) {
                        Text(
                            text = stringResource(R.string.action_delete),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    FilledTonalButton(onClick = { resetGameDataDialog = false }) {
                        Text(stringResource(R.string.action_cancel))
                    }
                },
                onDismissRequest = { resetGameDataDialog = false }
            )
        }
    }
}