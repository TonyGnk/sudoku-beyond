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

package gr.tonygnk.sudokubeyond.ui.app.bloc

import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.data.datastore.ThemeSettingsManager

class MainActivityBloc(
    blocContext: BlocContext,
    themeSettingsManager: ThemeSettingsManager,
    appSettingsManager: AppSettingsManager,
) : BlocContext by blocContext {
    val dc = themeSettingsManager.dynamicColors
    val darkTheme = themeSettingsManager.darkTheme
    val amoledBlack = themeSettingsManager.amoledBlack
    val firstLaunch = appSettingsManager.firstLaunch
    val monetSudokuBoard = themeSettingsManager.monetSudokuBoard
    val colorSeed = themeSettingsManager.themeColorSeed
    val paletteStyle = themeSettingsManager.themePaletteStyle
    val autoUpdateChannel = appSettingsManager.autoUpdateChannel
    val updateDismissedName = appSettingsManager.updateDismissedName

    companion object {
        operator fun invoke(blocContext: BlocContext) = MainActivityBloc(
            blocContext = blocContext,
            themeSettingsManager = LibreSudokuApp.appModule.themeSettingsManager,
            appSettingsManager = LibreSudokuApp.appModule.appSettingsManager
        )
    }
}