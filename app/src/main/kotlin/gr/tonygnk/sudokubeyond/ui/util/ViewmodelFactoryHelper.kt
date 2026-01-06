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

package gr.tonygnk.sudokubeyond.ui.util

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

fun <VM : ViewModel> viewModelBuilder(initializer: () -> VM): ViewModelBuilder<VM> {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return initializer() as T
        }
    }
    return ViewModelBuilder(factory)
}


@Composable
internal inline fun <reified VM : ViewModel> viewModel(builder: ViewModelBuilder<VM>): VM {
    return viewModel(factory = builder.delegate)
}

class ViewModelBuilder<out VM : ViewModel>(val delegate: ViewModelProvider.Factory)