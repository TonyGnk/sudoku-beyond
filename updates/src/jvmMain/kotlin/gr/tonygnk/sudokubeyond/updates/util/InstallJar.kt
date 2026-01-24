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

import java.awt.Desktop

/**
 * Result of attempting to install the latest JAR update.
 */
sealed class JvmInstallResult {
    /** Installation was initiated successfully. User should restart the app. */
    data object Success : JvmInstallResult()

    /** The JAR file was not found at the expected location. */
    data object FileNotFound : JvmInstallResult()

    /** Desktop API is not supported on this platform. */
    data object DesktopNotSupported : JvmInstallResult()

    /** An error occurred during installation. */
    data class Error(val message: String) : JvmInstallResult()
}

/**
 * Opens the downloaded JAR file location or the JAR file itself.
 * The user will need to manually close the current app and run the new JAR.
 *
 * This approach is chosen because:
 * 1. The running JAR cannot replace itself
 * 2. We want to avoid requiring elevated permissions
 * 3. It's the safest approach for a simple game app
 *
 * @return The result of the installation attempt
 */
fun installLatestJar(): JvmInstallResult {
    val jarFile = getLatestJar()

    if (!jarFile.exists()) {
        return JvmInstallResult.FileNotFound
    }

    return try {
        if (!Desktop.isDesktopSupported()) {
            return JvmInstallResult.DesktopNotSupported
        }

        val desktop = Desktop.getDesktop()

        // Open the folder containing the JAR file so the user can see it
        if (desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(jarFile.parentFile)
            JvmInstallResult.Success
        } else {
            // Fallback: try to browse to the file location
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(jarFile.parentFile.toURI())
                JvmInstallResult.Success
            } else {
                JvmInstallResult.DesktopNotSupported
            }
        }
    } catch (e: Exception) {
        JvmInstallResult.Error(e.message ?: "Unknown error")
    }
}

/**
 * Returns the path to the latest JAR file as a string for display to the user.
 * Useful for showing the user where the new version was downloaded.
 */
fun getLatestJarPath(): String = getLatestJar().absolutePath

/**
 * Checks if a downloaded JAR update exists.
 */
fun hasDownloadedJarUpdate(): Boolean = getLatestJar().exists()

/**
 * Deletes the downloaded JAR file (e.g., after successful update or to clean up).
 */
fun deleteDownloadedJar(): Boolean {
    val jarFile = getLatestJar()
    return if (jarFile.exists()) {
        jarFile.delete()
    } else {
        true
    }
}