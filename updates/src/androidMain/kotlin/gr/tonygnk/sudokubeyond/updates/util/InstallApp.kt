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

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider

fun installLatestApk(context: Context) = context.run {
    kotlin
        .runCatching {
            val contentUri =
                FileProvider.getUriForFile(
                    this,
                    "${context.packageName}.provider",
                    getLatestApk()
                )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setDataAndType(contentUri, "application/vnd.android.package-archive")
            }
            startActivity(intent)
        }
        .onFailure { throwable: Throwable ->
            throwable.printStackTrace()
        }
}
