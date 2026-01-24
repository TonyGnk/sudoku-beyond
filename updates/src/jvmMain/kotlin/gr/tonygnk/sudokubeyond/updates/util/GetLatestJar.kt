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
 * Returns the directory where update files should be stored for JVM Desktop.
 * Creates the directory if it doesn't exist.
 *
 * Location: ~/.sudoku-beyond/updates/
 */
fun getJvmUpdatesDirectory(): File {
    val userHome = System.getProperty("user.home")
    val updatesDir = File(userHome, ".sudoku-beyond/updates")
    if (!updatesDir.exists()) {
        updatesDir.mkdirs()
    }
    return updatesDir
}

/**
 * Returns the path to the latest downloaded JAR file for JVM Desktop.
 *
 * Location: ~/.sudoku-beyond/updates/latest.jar
 */
fun getLatestJar(): File {
    return File(getJvmUpdatesDirectory(), "latest.jar")
}
