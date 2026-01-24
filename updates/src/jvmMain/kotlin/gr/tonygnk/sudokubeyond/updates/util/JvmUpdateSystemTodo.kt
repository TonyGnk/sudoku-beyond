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

package gr.tonygnk.sudokubeyond.updates.util

import java.io.File

/**
 * Platform-specific file location utilities for JVM Desktop.
 *
 * These utilities are ready to be used when the app module is configured
 * for Kotlin Multiplatform with JVM Desktop support.
 */

// =============================================================================
// TODO: JVM Desktop UpdateSystem Implementation
// =============================================================================
//
// When the app module is ready for Kotlin Multiplatform, create UpdateSystem
// for JVM Desktop similar to the Android implementation in:
// app/src/nonFOSS/kotlin/gr/tonygnk/sudokubeyond/updates/UpdateSystem.kt
//
// The JVM Desktop UpdateSystem should:
// 1. Use UpdatesRepository.checkForUpdate() - same as Android
// 2. Use filterAssetForPlatform() for JAR asset selection - already implemented
// 3. Use downloadApp() with getLatestJar() as target file location
// 4. Use installLatestJar() to open the download folder
// 5. Use JvmInstallResult to handle installation outcome and notify user to restart
//
// Example implementation structure:
//
// internal object UpdateSystemDesktop : UpdateSystemContract {
//
//     override val canUpdate = true
//
//     override suspend fun getLatestRelease(
//         currentVersion: String,
//         allowBetas: Boolean
//     ): ReleaseBrief? {
//         return UpdatesRepository.checkForUpdate(
//             currentVersion = currentVersion,
//             allowBetas = allowBetas
//         ).toBrief()
//     }
//
//     override suspend fun downloadApp(downloadUrl: String): Flow<Int> {
//         return UpdatesRepository.downloadApp(
//             downloadUrl = downloadUrl,
//             getTargetFileLocation = ::getLatestJar
//         )
//     }
//
//     override fun installApp(): JvmInstallResult {
//         return installLatestJar()
//     }
//
//     // Note: The install function should notify the user to:
//     // 1. Close the current application
//     // 2. Run the new JAR from ~/.sudoku-beyond/updates/latest.jar
//     // 3. Optionally delete the old JAR after confirming the new one works
// }
//
// =============================================================================
