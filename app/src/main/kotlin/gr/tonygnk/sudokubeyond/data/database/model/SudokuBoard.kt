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

package gr.tonygnk.sudokubeyond.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import gr.tonygnk.sudokubeyond.core.qqwing.GameDifficulty
import gr.tonygnk.sudokubeyond.core.qqwing.GameType
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "board",
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = Folder::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("folder_id")
        )
    ]
)
data class SudokuBoard(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "initial_board") val initialBoard: String,
    @ColumnInfo(name = "solved_board") val solvedBoard: String,
    @ColumnInfo(name = "difficulty") val difficulty: GameDifficulty,
    @ColumnInfo(name = "type") val type: GameType,
    @ColumnInfo(name = "folder_id", defaultValue = "null") val folderId: Long? = null,
    @ColumnInfo(name = "killer_cages", defaultValue = "null") val killerCages: String? = null
)