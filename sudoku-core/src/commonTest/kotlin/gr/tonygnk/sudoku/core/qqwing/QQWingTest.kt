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

package gr.tonygnk.sudoku.core.qqwing

import gr.tonygnk.sudoku.core.algorithm.QQWing
import gr.tonygnk.sudoku.core.types.GameDifficulty
import gr.tonygnk.sudoku.core.types.GameType
import gr.tonygnk.sudoku.core.utils.SudokuParser
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class QQWingTest {

    @Test
    fun solve6x6_ReturnsTrue() {
        val board = SudokuParser().parseBoard(
            board = "500600000020053001100350040000001005",
            gameType = GameType.Default6x6,
            emptySeparator = '0'
        )

        val solvedBoard = SudokuParser().parseBoard(
            board = "532614416523653241124356345162261435",
            gameType = GameType.Default6x6,
            emptySeparator = '0'
        )

        val qqwing = QQWing(GameType.Default6x6, GameDifficulty.Unspecified)
        qqwing.setPuzzle(board.flatten().map { it.value }.toIntArray())
        qqwing.solve()

        assertTrue(solvedBoard.flatten().map { it.value }.toIntArray().contentEquals(qqwing.solution))
    }

    @Test
    fun solve6x6_NoSolution_ReturnsFalse() {
        val board = SudokuParser().parseBoard(
            board = "106020205001010602623100001250562010",
            gameType = GameType.Default6x6,
            emptySeparator = '0'
        )
        val qqwing = QQWing(GameType.Default6x6, GameDifficulty.Unspecified)
        qqwing.setPuzzle(board.flatten().map { it.value }.toIntArray())
        qqwing.solve()

        assertFalse(qqwing.isSolved())
    }


    @Test
    fun solve6x6_UniqueSolution_ReturnsTrue() {
        val board = SudokuParser().parseBoard(
            board = "500600000020053001100350040000001005",
            gameType = GameType.Default6x6,
            emptySeparator = '0'
        )
        val qqwing = QQWing(GameType.Default6x6, GameDifficulty.Unspecified)
        qqwing.setPuzzle(board.flatten().map { it.value }.toIntArray())
        qqwing.solve()
        assertTrue(qqwing.hasUniqueSolution())
    }

    @Test
    fun solve6x6_UniqueSolution_ReturnsFalse() {
        val board = SudokuParser().parseBoard(
            board = "000000000020053001100350040000001005",
            gameType = GameType.Default6x6,
            emptySeparator = '0'
        )
        val qqwing = QQWing(GameType.Default6x6, GameDifficulty.Unspecified)
        qqwing.setPuzzle(board.flatten().map { it.value }.toIntArray())
        qqwing.solve()

        assertFalse(qqwing.hasUniqueSolution())
    }

    /**
     * TODO:
     * Add test for:
     * 9x9 and 12x12
     * Generating
     * Solutions count
     * .......
     */
}