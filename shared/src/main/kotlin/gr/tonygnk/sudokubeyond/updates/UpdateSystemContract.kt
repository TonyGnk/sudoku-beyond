/*
 * Copyright (C) 2026 TonyGnk
 *
 * This file is part of Sudoku Beyond.
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

package gr.tonygnk.sudokubeyond.updates

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow

interface UpdateSystemContract {

    val canUpdate: Boolean

    suspend fun getLatestRelease(currentVersion: String, allowBetas: Boolean = true): ReleaseBrief?

    suspend fun downloadApk(context: Context, downloadUrl: String): Flow<Int>

    fun installApk(context: Context)

    fun String.toVersionName(): String?

    @Composable
    fun ReleaseBrief.MarkdownBody(modifier: Modifier = Modifier)

    companion object Companion {
        operator fun invoke(): UpdateSystemContract = UpdateSystem
    }
}