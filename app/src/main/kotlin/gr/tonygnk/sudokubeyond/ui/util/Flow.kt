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

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun <T : Any> Value<T>.toStateFlow(): StateFlow<T> = ValueStateFlow(this)

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
private class ValueStateFlow<out T : Any>(
    private val source: Value<T>,
) : StateFlow<T> {
    override val value: T
        get() = source.value

    override val replayCache: List<T>
        get() = listOf(source.value)

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        val flow = MutableStateFlow(source.value)
        val disposable = source.subscribe { flow.value = it }

        try {
            flow.collect(collector)
        } finally {
            disposable.cancel()
        }
    }
}