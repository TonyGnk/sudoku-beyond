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

package gr.tonygnk.sudokubeyond

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import gr.tonygnk.sudokubeyond.core.utils.GlobalExceptionHandler
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.destinations.ImportFromFileScreenDestination
import gr.tonygnk.sudokubeyond.ui.app.composable.MainActivityScreen
import gr.tonygnk.sudokubeyond.ui.app_crash.CrashActivity
import gr.tonygnk.sudokubeyond.ui.theme.SudokuBoardColorsImpl
import gr.tonygnk.sudokubeyond.ui.util.findActivity

val LocalBoardColors = staticCompositionLocalOf { SudokuBoardColorsImpl() }

class MainActivity : AppCompatActivity() {
    lateinit var settings: AppSettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (!BuildConfig.DEBUG) {
            GlobalExceptionHandler.initialize(applicationContext, CrashActivity::class.java)
        }

        val blocContext = defaultComponentContext()

        setContent {
            MainActivityScreen(blocContext)
        }
    }
}

@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "content://",
            mimeType = "*/*",
            action = Intent.ACTION_VIEW
        )
    ]
)
@Composable
fun HandleImportFromFileDeepLink(
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val activity = context.findActivity()
        if (activity != null) {
            val intentData = activity.intent.data
            if (intentData != null) {
                navigator.navigate(
                    ImportFromFileScreenDestination(
                        fileUri = intentData.toString(),
                        fromDeepLink = true
                    )
                )
            }
        }
    }
}