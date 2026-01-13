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

package gr.tonygnk.sudokubeyond.ui.settings.appearance

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.PreferencesConstants
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.SettingsBoardThemeConfig
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRow
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRowSwitch
import gr.tonygnk.sudokubeyond.ui.components.ScrollbarLazyColumn
import gr.tonygnk.sudokubeyond.ui.settings.AppThemeItem
import gr.tonygnk.sudokubeyond.ui.settings.DateFormatDialog
import gr.tonygnk.sudokubeyond.ui.settings.SelectionDialog
import gr.tonygnk.sudokubeyond.ui.settings.SetDateFormatPatternDialog
import gr.tonygnk.sudokubeyond.ui.settings.SettingsScaffoldLazyColumn
import gr.tonygnk.sudokubeyond.ui.settings.components.AppThemePreviewItem
import gr.tonygnk.sudokubeyond.ui.theme.LibreSudokuTheme
import java.time.ZonedDateTime
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun SettingsAppearanceScreen(
    bloc: SettingsAppearanceBloc,
    navigate: (MainActivityBloc.PagesConfig) -> Unit,
    finish: () -> Unit,
) {
    val context = LocalContext.current

    var darkModeDialog by rememberSaveable { mutableStateOf(false) }
    var dateFormatDialog by rememberSaveable { mutableStateOf(false) }
    var customFormatDialog by rememberSaveable { mutableStateOf(false) }
    var paletteStyleDialog by rememberSaveable { mutableStateOf(false) }

    val darkTheme by bloc.darkTheme.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_DARK_THEME)
    val dateFormat by bloc.dateFormat.collectAsStateWithLifecycle(initialValue = "")
    val dynamicColors by bloc.dynamicColors.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_DYNAMIC_COLORS)
    val amoledBlack by bloc.amoledBlack.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_AMOLED_BLACK)

    val currentPaletteStyle by bloc.paletteStyle.collectAsStateWithLifecycle(initialValue = PaletteStyle.TonalSpot)
    val currentSeedColor by bloc.seedColor.collectAsStateWithLifecycle(
        initialValue = Color(
            PreferencesConstants.DEFAULT_THEME_SEED_COLOR
        )
    )

    SettingsScaffoldLazyColumn(
        titleText = stringResource(R.string.pref_appearance),
        finish = finish
    ) { paddingValues ->
        ScrollbarLazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_dark_theme),
                    subtitle = when (darkTheme) {
                        0 -> stringResource(R.string.pref_dark_theme_follow)
                        1 -> stringResource(R.string.pref_dark_theme_off)
                        2 -> stringResource(R.string.pref_dark_theme_on)
                        else -> ""
                    },
                    onClick = { darkModeDialog = true },
                    drawableRes = R.drawable.ic_dark_mode
                )
            }

            item {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.pref_app_theme)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        item {
                            LibreSudokuTheme(
                                dynamicColor = true,
                                darkTheme = when (darkTheme) {
                                    0 -> isSystemInDarkTheme()
                                    1 -> false
                                    else -> true
                                },
                                amoled = amoledBlack
                            ) {
                                Column(
                                    modifier = Modifier
                                        .width(115.dp)
                                        .padding(start = 8.dp, end = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AppThemePreviewItem(
                                        selected = dynamicColors,
                                        onClick = {
                                            bloc.updateDynamicColors(true)
                                        },
                                        colorScheme = MaterialTheme.colorScheme,
                                        shapes = MaterialTheme.shapes
                                    )
                                    Text(
                                        text = stringResource(R.string.theme_dynamic),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                    items(
                        listOf(
                            Color.Green to context.getString(R.string.theme_green),
                            Color.Red to context.getString(R.string.theme_peach),
                            Color.Yellow to context.getString(R.string.theme_yellow),
                            Color.Blue to context.getString(R.string.theme_blue),
                            Color(0xFFC97820) to context.getString(R.string.theme_orange),
                            Color.Cyan to context.getString(R.string.theme_cyan),
                            Color.Magenta to context.getString(R.string.theme_lavender)
                        )
                    ) {
                        AppThemeItem(
                            title = it.second,
                            colorScheme = rememberDynamicColorScheme(
                                seedColor = it.first,
                                isDark = when (darkTheme) {
                                    0 -> isSystemInDarkTheme()
                                    1 -> false
                                    else -> true
                                },
                                style = currentPaletteStyle,
                                isAmoled = amoledBlack
                            ),
                            onClick = {
                                bloc.updateDynamicColors(false)
                                bloc.updateCurrentSeedColor(it.first)
                            },
                            selected = currentSeedColor == it.first && !dynamicColors,
                            amoledBlack = amoledBlack,
                            darkTheme = darkTheme,
                        )
                    }
                }
            }
            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_monet_style),
                    subtitle = when (currentPaletteStyle) {
                        PaletteStyle.TonalSpot -> stringResource(R.string.monet_tonalspot)
                        PaletteStyle.Neutral -> stringResource(R.string.monet_neutral)
                        PaletteStyle.Vibrant -> stringResource(R.string.monet_vibrant)
                        PaletteStyle.Expressive -> stringResource(R.string.monet_expressive)
                        PaletteStyle.Rainbow -> stringResource(R.string.monet_rainbow)
                        PaletteStyle.FruitSalad -> stringResource(R.string.monet_fruitsalad)
                        PaletteStyle.Monochrome -> stringResource(R.string.monet_monochrome)
                        PaletteStyle.Fidelity -> stringResource(R.string.monet_fidelity)
                        PaletteStyle.Content -> stringResource(R.string.monet_content)
                    },
                    onClick = { paletteStyleDialog = true },
                    drawableRes = R.drawable.ic_palette
                )
            }
            item {
                PreferenceRowSwitch(
                    title = stringResource(R.string.pref_pure_black),
                    checked = amoledBlack,
                    onClick = {
                        bloc.updateAmoledBlack(!amoledBlack)
                    },
                    drawableRes = R.drawable.ic_contrast
                )
            }
            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_board_theme_title),
                    subtitle = stringResource(R.string.pref_board_theme_summary),
                    onClick = {
                        navigate(SettingsBoardThemeConfig)
                    },
                    drawableRes = R.drawable.ic_grid
                )
            }
            item {
                PreferenceRow(
                    title = stringResource(R.string.pref_date_format),
                    subtitle = "${dateFormat.ifEmpty { stringResource(R.string.label_default) }} (${
                        ZonedDateTime.now().format(AppSettingsManager.dateFormat(dateFormat))
                    })",
                    onClick = { dateFormatDialog = true },
                    drawableRes = R.drawable.ic_edit_calendar
                )
            }
        }
    }

    if (darkModeDialog) {
        SelectionDialog(
            title = stringResource(R.string.pref_dark_theme),
            selections = listOf(
                stringResource(R.string.pref_dark_theme_follow),
                stringResource(R.string.pref_dark_theme_off),
                stringResource(R.string.pref_dark_theme_on)
            ),
            selected = darkTheme,
            onSelect = { index ->
                bloc.updateDarkTheme(index)
            },
            onDismiss = { darkModeDialog = false }
        )
    } else if (dateFormatDialog) {
        DateFormatDialog(
            title = stringResource(R.string.pref_date_format),
            entries = DateFormats.associateWith { dateFormatEntry ->
                val dateString = ZonedDateTime.now().format(
                    when (dateFormatEntry) {
                        "" -> {
                            DateTimeFormatter.ofPattern(
                                DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                                    FormatStyle.SHORT,
                                    null,
                                    IsoChronology.INSTANCE,
                                    Locale.getDefault()
                                )
                            )
                        }

                        else -> {
                            DateTimeFormatter.ofPattern(dateFormatEntry)
                        }
                    }
                )
                "${dateFormatEntry.ifEmpty { stringResource(R.string.label_default) }} ($dateString)"
            },
            customDateFormatText =
                if (!DateFormats.contains(dateFormat))
                    "$dateFormat (${
                        ZonedDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat))
                    })"
                else stringResource(R.string.pref_date_format_custom_label),
            selected = dateFormat,
            onSelect = { format ->
                if (format == "custom") {
                    customFormatDialog = true
                } else {
                    bloc.updateDateFormat(format)
                }
                dateFormatDialog = false
            },
            onDismiss = { dateFormatDialog = false },

            )
    } else if (paletteStyleDialog) {
        SelectionDialog(
            title = stringResource(R.string.pref_monet_style),
            selections = listOf(
                stringResource(R.string.monet_tonalspot),
                stringResource(R.string.monet_neutral),
                stringResource(R.string.monet_vibrant),
                stringResource(R.string.monet_expressive),
                stringResource(R.string.monet_rainbow),
                stringResource(R.string.monet_fruitsalad),
                stringResource(R.string.monet_monochrome),
                stringResource(R.string.monet_fidelity),
                stringResource(R.string.monet_content)
            ),
            selected = ThemeSettingsManager.paletteStyles.find { it.first == currentPaletteStyle }?.second
                ?: 0,
            onSelect = { index ->
                bloc.updatePaletteStyle(index)
            },
            onDismiss = { paletteStyleDialog = false }
        )
    }

    if (customFormatDialog) {
        var customDateFormat by rememberSaveable {
            mutableStateOf(
                if (DateFormats.contains(
                        dateFormat
                    )
                ) "" else dateFormat
            )
        }
        var invalidCustomDateFormat by rememberSaveable { mutableStateOf(false) }
        var dateFormatPreview by rememberSaveable { mutableStateOf("") }

        SetDateFormatPatternDialog(
            onConfirm = {
                if (bloc.checkCustomDateFormat(customDateFormat)) {
                    bloc.updateDateFormat(customDateFormat)
                    invalidCustomDateFormat = false
                    customFormatDialog = false
                } else {
                    invalidCustomDateFormat = true
                }
            },
            onDismissRequest = { customFormatDialog = false },
            onTextValueChange = { text ->
                customDateFormat = text
                if (invalidCustomDateFormat) invalidCustomDateFormat = false

                dateFormatPreview = if (bloc.checkCustomDateFormat(customDateFormat)) {
                    ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern(customDateFormat))
                } else {
                    ""
                }
            },
            customDateFormat = customDateFormat,
            invalidCustomDateFormat = invalidCustomDateFormat,
            datePreview = dateFormatPreview
        )
    }
}

private val DateFormats = listOf(
    "",
    "dd/MM/yy",
    "dd.MM.yy",
    "MM/dd/yy",
    "yyyy-MM-dd",
    "dd MMM yyyy",
    "MMM dd, yyyy"
)