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

package gr.tonygnk.sudokubeyond

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.asJetpackComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@OptIn(ExperimentalDecomposeApi::class)
fun main() {
    System.setProperty("apple.awt.application.appearance", "system")

    val lifecycle = LifecycleRegistry()

    val blocContext = runOnUiThread {
        DefaultComponentContext(lifecycle = lifecycle)
    }.asJetpackComponentContext()

    application {
        Window(
            title = "Sudoku Beyond",
            alwaysOnTop = false,
            onCloseRequest = ::exitApplication,
        ) {

        }
    }
}