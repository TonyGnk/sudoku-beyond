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

package gr.tonygnk.sudokubeyond.domain.usecase.update

import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.UpdateChannel
import gr.tonygnk.sudokubeyond.updates.ReleaseBrief
import gr.tonygnk.sudokubeyond.updates.UpdateSystem
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class UpdateCheckUseCase(
    private val appSettingsManager: AppSettingsManager,
) {
    /**
     * Check for available updates with intelligent caching.
     *
     * @param currentVersion The current app version
     * @param allowBetas Whether to check for beta releases
     * @param forceRefresh If true, bypasses cache and performs fresh check (used in AutoUpdateScreen)
     * @return ReleaseBrief if update is available, null otherwise or on error
     */
    suspend operator fun invoke(
        currentVersion: String,
        allowBetas: Boolean,
        forceRefresh: Boolean = false,
    ): Result {
        val now = Clock.System.now().toEpochMilliseconds()

        // Check if we're still in error cooldown period
        val lastError = appSettingsManager.lastUpdateCheckError.first()
        val lastErrorTimestamp = appSettingsManager.lastUpdateCheckErrorTimestamp.first()
        if (lastError && !forceRefresh && (now - lastErrorTimestamp) < ERROR_RETRY_DURATION_MS) {
            return Result.CachedError
        }

        // Determine the channel for this check
        val currentChannel = if (allowBetas) UpdateChannel.Beta else UpdateChannel.Stable

        if (!forceRefresh) {
            // Check if we have valid cached data
            val lastCheckTimestamp = appSettingsManager.lastUpdateCheckTimestamp.first()
            val cachedChannel = appSettingsManager.cachedUpdateCheckChannel.first()
            val cacheAge = now - lastCheckTimestamp

            // Convert cachedChannel int to UpdateChannel
            val cachedChannelEnum = when (cachedChannel) {
                0 -> UpdateChannel.Disabled
                1 -> UpdateChannel.Stable
                2 -> UpdateChannel.Beta
                else -> null
            }

            // Cache is valid if: within time limit AND same channel
            if (cacheAge < CACHE_DURATION_MS && cachedChannelEnum == currentChannel) {
                // Return cached data
                val name = appSettingsManager.cachedReleaseName.first()
                val downloadUrl = appSettingsManager.cachedReleaseDownloadUrl.first()
                val htmlUrl = appSettingsManager.cachedReleaseHtmlUrl.first()
                val body = appSettingsManager.cachedReleaseBody.first()

                // If all fields are present, return cached release
                if (!name.isNullOrEmpty() && !downloadUrl.isNullOrEmpty() &&
                    !htmlUrl.isNullOrEmpty() && !body.isNullOrEmpty()
                ) {
                    return Result.Success(
                        ReleaseBrief(
                            name = name,
                            downloadUrl = downloadUrl,
                            htmlUrl = htmlUrl,
                            body = body
                        )
                    )
                } else if (name.isNullOrEmpty()) {
                    // Cached result was "no update available"
                    return Result.Success(null)
                }
            }
        }

        // Cache is invalid or force refresh - perform network check
        return try {
            val release = UpdateSystem.getLatestRelease(currentVersion, allowBetas)

            // Cache the result
            if (release != null) {
                appSettingsManager.setCachedUpdateRelease(
                    releaseName = release.name,
                    downloadUrl = release.downloadUrl,
                    htmlUrl = release.htmlUrl,
                    body = release.body,
                    channel = currentChannel,
                    timestamp = now
                )
            } else {
                // Cache "no update available" result
                appSettingsManager.setCachedUpdateRelease(
                    releaseName = null,
                    downloadUrl = null,
                    htmlUrl = null,
                    body = null,
                    channel = currentChannel,
                    timestamp = now
                )
            }

            Result.Success(release)
        } catch (e: Exception) {
            // Cache error state to avoid hammering the API
            appSettingsManager.setUpdateCheckError(now)
            Result.Error(e)
        }
    }

    sealed class Result {
        data class Success(val release: ReleaseBrief?) : Result()
        data class Error(val exception: Exception) : Result()
        data object CachedError : Result()
    }

    companion object {
        private val CACHE_DURATION_MS = 7.days.inWholeMilliseconds
        private val ERROR_RETRY_DURATION_MS = 6.hours.inWholeMilliseconds
    }
}