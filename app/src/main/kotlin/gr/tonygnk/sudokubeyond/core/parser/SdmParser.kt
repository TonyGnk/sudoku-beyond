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

package gr.tonygnk.sudokubeyond.core.parser

import android.util.Log
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard

/**
 * .sdm - is a very simple format.
 * Each line is a single puzzle.
 * Empty cells can be represented by a zero or a dot
 * Example: 000605000003020800045090270500000001062000540400000007098060450006040700000203000
 */
class SdmParser : FileImportParser {
    private val tag = "SDMParser"

    /**
     * @param content .sdm file content
     * @return Pair with: First - parsing success. Second - strings of parsed boards
     */
    override fun toBoards(content: String): Pair<Boolean, List<String>> {
        val toImport = mutableListOf<String>()
        if (content.isEmpty()) return Pair(false, toImport)

        try {
            content.lines().forEach {
                val line = it.trim()

                if (line.length == 81 && line.all { char -> char.isDigit() }) {
                    toImport.add(line.replace(".", "0"))
                } else {
                    Log.i(tag, "This line was skipped: $line")
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Exception while parsing!")
            e.printStackTrace()
            return Pair(false, toImport)
        }

        return Pair(true, toImport)
    }

    fun boardsToSdm(boards: List<SudokuBoard>): String {
        val stringBuilder = StringBuilder()
        boards.forEach { game ->
            stringBuilder.append(game.initialBoard + "\n")
        }
        // Remove the extra \n that was added in the loop above
        stringBuilder.delete(stringBuilder.length - 1, stringBuilder.length)
        return stringBuilder.toString()
    }
}