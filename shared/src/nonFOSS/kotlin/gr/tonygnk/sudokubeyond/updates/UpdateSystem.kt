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
import gr.tonygnk.sudokubeyond.updates.model.Release
import gr.tonygnk.sudokubeyond.updates.model.toVersion
import gr.tonygnk.sudokubeyond.updates.repository.UpdatesRepository
import gr.tonygnk.sudokubeyond.updates.ui.UpdateMarkdown
import gr.tonygnk.sudokubeyond.updates.util.filterAssetForPlatform
import gr.tonygnk.sudokubeyond.updates.util.getLatestApk
import gr.tonygnk.sudokubeyond.updates.util.installLatestApk
import kotlinx.coroutines.flow.Flow

internal object UpdateSystem : UpdateSystemContract {

    override val canUpdate = true

    override suspend fun getLatestRelease(currentVersion: String, allowBetas: Boolean): ReleaseBrief? {
        return UpdatesRepository.checkForUpdate(
            currentVersion = currentVersion, allowBetas = allowBetas
        ).toBrief()
    }

    override suspend fun downloadApk(context: Context, downloadUrl: String): Flow<Int> {
        return UpdatesRepository.downloadApp(
            downloadUrl = downloadUrl,
            getTargetFileLocation = context::getLatestApk
        )
    }

    override fun installApk(context: Context) {
        installLatestApk(context)
    }

    override fun String.toVersionName(): String = this.toVersion().toVersionString()

    @Composable
    override fun ReleaseBrief.MarkdownBody(modifier: Modifier) {
        UpdateMarkdown(
            body = this.body,
            modifier = modifier
        )
    }
}

private fun Release?.toBrief(): ReleaseBrief? {
    val name = this?.name ?: return null
    val downloadUrl = filterAssetForPlatform(this.assets) ?: return null
    val htmlUrl = this.htmlUrl ?: return null
    val body = this.body ?: return null

    return ReleaseBrief(
        name = name,
        downloadUrl = downloadUrl,
        htmlUrl = htmlUrl,
        body = body
    )
}