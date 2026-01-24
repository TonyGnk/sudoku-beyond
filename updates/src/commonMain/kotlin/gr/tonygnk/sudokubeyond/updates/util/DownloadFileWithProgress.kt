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

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

fun downloadFileWithProgress(response: HttpResponse, getTargetFile: () -> Any): Flow<Int> {
    return flow {
        emit(0)

        val channel = response.bodyAsChannel()
        val totalBytes = response.contentLength() ?: -1

        val success = writeBytesToFile(
            channel = channel,
            totalBytes = totalBytes,
            onProgress = { progress -> emit(progress) },
            getTargetFile = getTargetFile
        )

        if (success) {
            emit(100)
        } else {
            throw Exception("Failed to write file")
        }
    }
        .distinctUntilChanged()
}

expect suspend fun writeBytesToFile(
    channel: ByteReadChannel,
    totalBytes: Long,
    onProgress: suspend (Int) -> Unit,
    getTargetFile: () -> Any,
): Boolean