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

import gr.tonygnk.sudokubeyond.updates.model.Release
import gr.tonygnk.sudokubeyond.updates.model.Version
import gr.tonygnk.sudokubeyond.updates.model.toVersion
import gr.tonygnk.sudokubeyond.updates.util.downloadFileWithProgress
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.prepareGet
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.json.Json

object UpdatesRepository {
    private const val OWNER = "kaajjo"
    private const val REPO = "LibreSudoku"

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
    }

    suspend fun checkForUpdate(currentVersion: String, allowBetas: Boolean = true): Release? {
        val currentVersionParsed = currentVersion.toVersion()

        val latestRelease = getLatestRelease(allowBetas)
        val latestVersion = latestRelease.name?.toVersion() ?: Version.Stable(0, 0, 0)
        return if (latestVersion > currentVersionParsed) {
            latestRelease
        } else null
    }

    private suspend fun getLatestRelease(allowBetas: Boolean): Release {
        val releases: List<Release> = httpClient
            .get("https://api.github.com/repos/$OWNER/$REPO/releases")
            .body()

        return releases
            .filter { release ->
                println(release.name ?: "null")
                if (!allowBetas && release.name != null)
                    release.name.toVersion() is Version.Stable
                else
                    true
            }
            .maxBy { it.name?.toVersion()?.toVersionNumber() ?: 0 }
            .also {
                println(it.name?.toVersion()?.toVersionString() ?: "null")
            }
    }

    suspend fun downloadApp(
        downloadUrl: String,
        getTargetFileLocation: () -> Any,
    ): Flow<Int> {
        return try {
            httpClient.prepareGet(downloadUrl).execute { response ->
                downloadFileWithProgress(response, getTargetFileLocation)
            }
        } catch (e: Exception) {
            println("Failed to download the apk: ${e.message}")
            emptyFlow()
        }
    }
}