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
import kotlinx.coroutines.flow.flow

internal object UpdateSystem : UpdateSystemContract {

    override val canUpdate = true

    override suspend fun hasUndismissedUpdate(
        currentVersion: String,
        allowBetas: Boolean,
        updateDismissedName: String,
    ): Boolean = false

    override suspend fun getAvailableUpdateRelease(
        currentVersion: String,
        allowBetas: Boolean,
    ): ReleaseBrief? = null

    override suspend fun downloadApk(
        context: Context,
        downloadUrl: String,
    ): Flow<ReleaseDownloadStatus> = flow {
        emit(ReleaseDownloadStatus.NotStarted)
    }

    override fun installApk(context: Context) {}

    override fun String.toVersionName(): String? = null

    @Composable
    override fun ReleaseBrief.MarkdownBody(modifier: Modifier) {
    }
}