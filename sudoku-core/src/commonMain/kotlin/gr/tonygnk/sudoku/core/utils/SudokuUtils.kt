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

package gr.tonygnk.sudoku.core.utils

import gr.tonygnk.sudoku.core.model.Cell
import gr.tonygnk.sudoku.core.model.Note
import gr.tonygnk.sudoku.core.types.GameType

object SudokuUtils {
    fun getBoxRowRange(cell: Cell, sectionHeight: Int): IntRange {
        val boxRow = cell.row / sectionHeight
        val startRow = boxRow * sectionHeight
        return startRow until startRow + sectionHeight
    }

    fun getBoxColRange(cell: Cell, sectionWidth: Int): IntRange {
        val boxCol = cell.col / sectionWidth
        val startCol = boxCol * sectionWidth
        return startCol until startCol + sectionWidth
    }

    fun getCandidates(board: List<List<Cell>>, cell: Cell, type: GameType): Set<Int> {
        if (cell.value != 0) return emptySet()

        val candidates = mutableSetOf<Int>()
        for (i in 1..type.size) {
            candidates.add(i)
        }

        // row - horizontal
        for (i in 0 until type.size) {
            candidates.remove(board[cell.row][i].value)
        }

        // col - vertical
        for (i in 0 until type.size) {
            candidates.remove(board[i][cell.col].value)
        }

        // box
        for (i in getBoxRowRange(cell, type.sectionHeight)) {
            for (j in getBoxColRange(cell, type.sectionWidth)) {
                candidates.remove(board[i][j].value)
            }
        }

        return candidates
    }

    fun isValidCellDynamic(board: List<List<Cell>>, cell: Cell, type: GameType): Boolean {
        if (cell.value == 0) return true

        // horizontal
        for (i in 0 until type.size) {
            if (i != cell.col && board[cell.row][i].value == cell.value) {
                return false
            }
        }

        // vertical
        for (i in 0 until type.size) {
            if (i != cell.row && board[i][cell.col].value == cell.value) {
                return false
            }
        }

        // box
        for (i in getBoxRowRange(cell, type.sectionHeight)) {
            for (j in getBoxColRange(cell, type.sectionWidth)) {
                if ((i != cell.row || j != cell.col) && board[i][j].value == cell.value) {
                    return false
                }
            }
        }
        return true
    }

    // returns count of given number on board
    fun countNumberInBoard(board: List<List<Cell>>, number: Int): Int {
        var count = 0
        board.forEach { cells ->
            cells.forEach {
                if (it.value == number) {
                    count++
                }
            }
        }
        return count
    }

    // compute all candidates for empty cells and returns them as notes
    fun computeNotes(board: List<List<Cell>>, type: GameType): List<Note> {
        var notes = emptyList<Note>()
        board.forEach { cells ->
            cells.forEach { cell ->
                if (cell.value == 0) {
                    getCandidates(board, cell, type).forEach {
                        notes = notes.plus(Note(cell.row, cell.col, it))
                    }
                }
            }
        }
        return notes
    }

    fun autoEraseNotes(
        board: List<List<Cell>>,
        notes: List<Note>,
        cell: Cell,
        type: GameType
    ): List<Note> {
        var newNotes = notes

        for (i in getBoxRowRange(cell, type.sectionHeight)) {
            for (j in getBoxColRange(cell, type.sectionWidth)) {
                if (board[i][j].value == 0 && newNotes.contains(Note(i, j, cell.value))) {
                    newNotes = newNotes.minus(Note(i, j, cell.value))
                }
            }
        }
        for (i in 0 until type.size) {
            if (board[i][cell.col].value == 0 && newNotes.contains(Note(i, cell.col, cell.value))) {
                newNotes = newNotes.minus(Note(i, cell.col, cell.value))
            }
            if (board[cell.row][i].value == 0 && newNotes.contains(Note(cell.row, i, cell.value))) {
                newNotes = newNotes.minus(Note(cell.row, i, cell.value))
            }
        }
        return newNotes
    }
}
