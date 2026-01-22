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
import kotlinx.coroutines.flow.map

internal object UpdateSystem : UpdateSystemContract {

    override val canUpdate = true

    override suspend fun hasUndismissedUpdate(
        currentVersion: String,
        allowBetas: Boolean,
        updateDismissedName: String,
    ): Boolean {
        val release = UpdatesManager.checkForUpdate(
            currentVersion = currentVersion,
            allowBetas = allowBetas
        )
        return release != null && release.name != updateDismissedName
    }

    override suspend fun getAvailableUpdateRelease(currentVersion: String, allowBetas: Boolean): ReleaseBrief? {
        return UpdatesManager.checkForUpdate(
            currentVersion = currentVersion, allowBetas = allowBetas
        ).toBrief()
    }


    override suspend fun downloadApk(context: Context, downloadUrl: String): Flow<ReleaseDownloadStatus> {
        return UpdatesManager.downloadApk(
            context = context, downloadUrl = downloadUrl
        ).toReleaseDownloadStatusFlow()
    }


    override fun installApk(context: Context) {
        UpdatesManager.installLatestApk(context)
    }

    override fun String.toVersionName(): String = this.toVersion().toVersionString()

    @Composable
    override fun ReleaseBrief.MarkdownBody(modifier: Modifier) {
        Markdown(
            body = this.body,
            modifier = modifier
        )
    }
}

private fun Release?.toBrief(): ReleaseBrief? {
    val name = this?.name ?: return null
    val downloadUrl = this.assets
        ?.firstOrNull { it.name?.lowercase()?.contains("nonfoss") ?: false }
        ?.browserDownloadUrl ?: return null
    val htmlUrl = this.htmlUrl ?: return null
    val body = this.body ?: return null

    return ReleaseBrief(
        name = name,
        downloadUrl = downloadUrl,
        htmlUrl = htmlUrl,
        body = body
    )
}

private fun Flow<DownloadStatus>.toReleaseDownloadStatusFlow(): Flow<ReleaseDownloadStatus> {
    return this.map { status ->
        when (status) {
            is DownloadStatus.Progress -> ReleaseDownloadStatus.Progress(status.percent)
            is DownloadStatus.Finished -> ReleaseDownloadStatus.Finished
        }
    }
}