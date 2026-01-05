/*
 * Copyright (C) 2022-2025 kaajjo
 * Copyright (C) 2026 TonyGnk
 *
 * This file is part of Sudoku Beyond.
 * Originally from LibreSudoku (https://github.com/kaajjo/LibreSudoku)
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

package com.kaajjo.libresudoku.data.backup

import com.kaajjo.libresudoku.data.backup.serializer.ZonedDateTimeLongSerializer
import com.kaajjo.libresudoku.data.database.model.Folder
import com.kaajjo.libresudoku.data.database.model.Record
import com.kaajjo.libresudoku.data.database.model.SavedGame
import com.kaajjo.libresudoku.data.database.model.SudokuBoard
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

const val BACKUP_SCHEME_VERSION = 1

@Serializable
data class BackupData(
    val appVersionName: String,
    val appVersionCode: Int,
    val backupSchemeVersion: Int = BACKUP_SCHEME_VERSION,
    @Serializable(with = ZonedDateTimeLongSerializer::class)
    val createdAt: ZonedDateTime,
    val boards: List<SudokuBoard>,
    val records: List<Record> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val savedGames: List<SavedGame>,
    val settings: SettingsBackup? = null
) {
    companion object {
        /**
         * Regex for auto backups
         */
        val regexAuto = """LibreSudoku-AutoBackup-\d+-\d+-\d+--\d+-\d+-\d+.json""".toRegex()

        /**
         * Filename for manual backups
         */
        val nameManual: String
            get() = "LibreSudoku-Backup-${
                ZonedDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss"))

            }"

        /**
         * Filename for auto backups
         */
        val nameAuto: String
            get() = "LibreSudoku-AutoBackup-${
            ZonedDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss"))
        }"
    }
}