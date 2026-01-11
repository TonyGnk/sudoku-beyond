/*
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

@file:OptIn(ExperimentalDecomposeApi::class)

package gr.tonygnk.sudokubeyond.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.StackAnimationScope
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV2
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.essenty.backhandler.BackHandler
import gr.tonygnk.sudokubeyond.core.BlocContext

@Stable
data class ChildStackState<C : Any, B : Any>(
    val blocContext: BlocContext,
    val stack: ChildStack<C, B>,
    val onChildSelect: (C) -> Unit,
    val backHandler: BackHandler,
    val onBackClick: () -> Unit,
)

@Composable
fun <C : Any, B : Any> ChildStackState<C, B>.ChildStack(
    modifier: Modifier = Modifier,
    content: @Composable (StackAnimationScope.(Child.Created<C, B>) -> Unit),
) {
    ChildStack(
        stack = stack,
        modifier = modifier,
        animation = stackAnimation(
            animator = fade(),
            predictiveBackParams = {
                PredictiveBackParams(
                    backHandler = backHandler,
                    onBack = onBackClick,
                    animatable = { backEvent -> androidPredictiveBackAnimatableV2(backEvent) },
                )
            },
        ),
        content = content,
    )
}