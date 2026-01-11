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

package gr.tonygnk.sudokubeyond.ui.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.AutoUpdateConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.SettingsAppearanceConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.SettingsAssistanceConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.SettingsGameplayConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.SettingsLanguageConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.SettingsOtherConfig
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRow
import gr.tonygnk.sudokubeyond.ui.components.ScrollbarLazyColumn
import gr.tonygnk.sudokubeyond.ui.components.collapsing_topappbar.CollapsingTitle
import gr.tonygnk.sudokubeyond.ui.components.collapsing_topappbar.CollapsingTopAppBar
import gr.tonygnk.sudokubeyond.ui.components.collapsing_topappbar.rememberTopAppBarScrollBehavior
import gr.tonygnk.sudokubeyond.ui.settings.components.AppThemePreviewItem
import gr.tonygnk.sudokubeyond.ui.util.getCurrentLocaleString
import gr.tonygnk.sudokubeyond.util.FlavorUtil

data class SettingsCategoriesBloc(val launchedFromGame: Boolean) : MainActivityBloc.PagesBloc

@Composable
fun SettingsCategoriesScreen(
    bloc: SettingsCategoriesBloc,
    navigate: (MainActivityBloc.PagesConfig) -> Unit,
    finish: () -> Unit,
) {
    val context = LocalContext.current
    val currentLanguage by remember { mutableStateOf(getCurrentLocaleString(context)) }
    SettingsScaffoldLazyColumn(
        titleText = stringResource(R.string.settings_title),
        finish = finish
    ) { paddingValues ->
        ScrollbarLazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_appearance),
                    subtitle = stringResource(R.string.perf_appearance_summary),
                    onClick = {
                        navigate(SettingsAppearanceConfig)
                    },
                    painter = rememberVectorPainter(Icons.Outlined.Palette)
                )
            }

            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_gameplay),
                    subtitle = stringResource(R.string.perf_gameplay_summary),
                    onClick = {
                        navigate(SettingsGameplayConfig)
                    },
                    painter = rememberVectorPainter(Icons.Outlined.Extension)
                )
            }

            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_assistance),
                    subtitle = stringResource(R.string.perf_assistance_summary),
                    onClick = {
                        navigate(SettingsAssistanceConfig)
                    },
                    painter = rememberVectorPainter(Icons.Outlined.TipsAndUpdates)
                )
            }

            item {
                PreferenceRow(
                    title = stringResource(R.string.advanced_hint_title),
                    subtitle = stringResource(R.string.advanced_hint_summary),
                    onClick = {
                        navigate(MainActivityBloc.PagesConfig.SettingsAdvancedHintConfig)
                    },
                    painter = rememberVectorPainter(Icons.Rounded.AutoAwesome)
                )
            }

            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_app_language),
                    subtitle = currentLanguage,
                    onClick = {
                        navigate(SettingsLanguageConfig)
                    },
                    painter = rememberVectorPainter(Icons.Outlined.Language)
                )
            }
            if (!FlavorUtil.isFoss()) {
                item {
                    PreferenceRow(
                        title = stringResource(R.string.auto_update_title),
                        subtitle = stringResource(R.string.auto_updates_summary),
                        onClick = {
                            navigate(AutoUpdateConfig)
                        },
                        painter = rememberVectorPainter(Icons.Rounded.SystemUpdate)
                    )
                }
            }
            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_other),
                    subtitle = stringResource(R.string.perf_other_summary),
                    onClick = {
                        navigate(SettingsOtherConfig(launchedFromGame = bloc.launchedFromGame))
                    },
                    painter = rememberVectorPainter(Icons.Outlined.MoreHoriz)
                )
            }
        }
    }
}

@Composable
fun SettingsCategory(
    modifier: Modifier = Modifier,
    title: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp, top = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun AppThemeItem(
    title: String,
    colorScheme: ColorScheme,
    amoledBlack: Boolean,
    darkTheme: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(115.dp)
            .padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppThemePreviewItem(
            selected = selected,
            onClick = onClick,
            colorScheme = colorScheme.copy(
                background =
                    if (amoledBlack && (darkTheme == 0 && isSystemInDarkTheme() || darkTheme == 2)) {
                        Color.Black
                    } else {
                        colorScheme.background
                    }
            ),
            shapes = MaterialTheme.shapes
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun SettingsScaffoldLazyColumn(
    finish: () -> Unit,
    titleText: String,
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scrollBehavior = rememberTopAppBarScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(it)
            }
        },
        topBar = {
            CollapsingTopAppBar(
                collapsingTitle = CollapsingTitle.medium(titleText = titleText),
                navigationIcon = {
                    IconButton(onClick = finish) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}