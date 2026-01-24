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

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import java.io.File

actual suspend fun writeBytesToFile(
    channel: ByteReadChannel,
    totalBytes: Long,
    onProgress: suspend (Int) -> Unit,
    getTargetFile: () -> Any,
): Boolean {
    val saveFile = getTargetFile() as File
    var deleteFile = true

    try {
        saveFile.outputStream().use { outputStream ->
            val buffer = ByteArray(8_192)
            var progressBytes = 0L

            while (!channel.isClosedForRead) {
                val bytes = channel.readAvailable(buffer, 0, buffer.size)
                if (bytes <= 0) continue

                outputStream.write(buffer, 0, bytes)
                progressBytes += bytes

                if (totalBytes > 0) {
                    onProgress(((progressBytes * 100) / totalBytes).toInt())
                }
            }

            when {
                totalBytes > 0 && progressBytes < totalBytes -> throw Exception("missing bytes")
                totalBytes in 1..<progressBytes -> throw Exception("too many bytes")
                else -> deleteFile = false
            }
        }

        return true
    } catch (e: Exception) {
        return false
    } finally {
        if (deleteFile) {
            saveFile.delete()
        }
    }
}