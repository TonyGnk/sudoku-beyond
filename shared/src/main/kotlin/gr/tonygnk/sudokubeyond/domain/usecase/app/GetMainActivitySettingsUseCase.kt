/*
 *  Copyright (C) 2026 TonyGnk
 *
 *  This file is part of Sudoku Beyond.
 *  Originally from LibreSudoku (https://github.com/kaajjo/LibreSudoku)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */

package gr.tonygnk.sudokubeyond.domain.usecase.app

import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager
import gr.tonygnk.sudokubeyond.domain.model.MainActivitySettings
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.UpdateChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMainActivitySettingsUseCase(
    private val themeSettingsManager: ThemeSettingsManager,
    private val appSettingsManager: AppSettingsManager,
) {
    operator fun invoke(): Flow<MainActivitySettings> = combine(
        themeSettingsManager.dynamicColors,
        themeSettingsManager.darkTheme,
        themeSettingsManager.amoledBlack,
        appSettingsManager.firstLaunch,
        themeSettingsManager.monetSudokuBoard,
        themeSettingsManager.themeColorSeed,
        themeSettingsManager.themePaletteStyle,
        appSettingsManager.autoUpdateChannel,
        appSettingsManager.updateDismissedName,
    ) { array ->
        MainActivitySettings(
            dynamicColors = array[0] as Boolean,
            darkTheme = array[1] as Int,
            amoledBlack = array[2] as Boolean,
            firstLaunch = array[3] as Boolean,
            monetSudokuBoard = array[4] as Boolean,
            colorSeed = array[5] as Color,
            paletteStyle = array[6] as PaletteStyle,
            autoUpdateChannel = array[7] as UpdateChannel,
            updateDismissedName = array[8] as String,
        )
    }
}