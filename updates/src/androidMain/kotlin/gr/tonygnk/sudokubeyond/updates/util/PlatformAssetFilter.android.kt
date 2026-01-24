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

import gr.tonygnk.sudokubeyond.updates.model.AssetsItem

/**
 * Android implementation: filters for APK with "nonfoss" in the name
 */
actual fun filterAssetForPlatform(assets: List<AssetsItem>?): String? {
    return assets
        ?.firstOrNull { asset ->
            val name = asset.name?.lowercase() ?: return@firstOrNull false
            name.contains("nonfoss") && name.endsWith(".apk")
        }
        ?.browserDownloadUrl
}