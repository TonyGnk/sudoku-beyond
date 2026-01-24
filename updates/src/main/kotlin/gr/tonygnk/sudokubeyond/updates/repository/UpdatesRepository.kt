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

package gr.tonygnk.sudokubeyond.updates.repository

import android.content.Context
import android.util.Log
import gr.tonygnk.sudokubeyond.updates.model.Release
import gr.tonygnk.sudokubeyond.updates.model.Version
import gr.tonygnk.sudokubeyond.updates.model.toVersion
import gr.tonygnk.sudokubeyond.updates.util.downloadFileWithProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

object UpdatesRepository {
    private const val OWNER = "kaajjo"
    private const val REPO = "LibreSudoku"

    private val requestReleases =
        Request.Builder()
            .url("https://api.github.com/repos/$OWNER/$REPO/releases")
            .build()

    private val okHttpClient = OkHttpClient()

    private val jsonFormat = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun checkForUpdate(currentVersion: String, allowBetas: Boolean = true): Release? {
        val currentVersionParsed = currentVersion.toVersion()

        val latestRelease = getLatestRelease(allowBetas)
        val latestVersion = latestRelease.name?.toVersion() ?: Version.Stable(0, 0, 0)
        return if (latestVersion > currentVersionParsed) {
            latestRelease
        } else null
    }

    private suspend fun getLatestRelease(allowBetas: Boolean): Release = withContext(Dispatchers.IO) {
        okHttpClient
            .newCall(requestReleases)
            .execute()
            .body.use { releasesResponse ->
                val releases = jsonFormat.decodeFromString<List<Release>>(releasesResponse.string())
                return@withContext releases
                    .filter { release ->
                        Log.d("UpdatesManager:filter", release.name ?: "null")
                        if (!allowBetas && release.name != null)
                            release.name.toVersion() is Version.Stable
                        else
                            true
                    }
                    .maxBy { it.name?.toVersion()?.toVersionNumber() ?: 0 }
                    .also {
                        Log.d("UpdatesManager:maxBy", it.name?.toVersion()?.toVersionString() ?: "null")
                    }
            }
    }

    suspend fun downloadApk(
        context: Context,
        downloadUrl: String,
    ): Flow<Int> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(downloadUrl)
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body
            return@withContext responseBody.downloadFileWithProgress(context.getLatestApk())
        } catch (e: Exception) {
            Log.e("UpdatesManager", "Failed to download the apk", e)
        }
        emptyFlow()
    }
}

internal fun Context.getLatestApk() = File(getExternalFilesDir("apk"), "latest.apk")