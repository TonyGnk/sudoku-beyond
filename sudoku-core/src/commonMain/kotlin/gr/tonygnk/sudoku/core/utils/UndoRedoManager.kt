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
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val board: List<List<Cell>>,
    val notes: List<Note>
)

class UndoRedoManager(
    private var undoStack: MutableList<GameState>,
    private var redoStack: MutableList<GameState>,
) {
    constructor() : this(mutableListOf(), mutableListOf())

    fun undo(current: GameState): GameState? {
        if (undoStack.isEmpty()) {
            return null
        }
        redoStack.add(current)
        return undoStack.removeLast()
    }

    fun redo(current: GameState): GameState? {
        if (redoStack.isEmpty()) {
            return null
        }
        undoStack.add(current)
        return redoStack.removeLast()
    }

    fun addState(current: GameState) {
        undoStack.add(current)
        redoStack.clear()
    }

    fun canUndo(): Boolean = undoStack.isNotEmpty()
    fun canRedo(): Boolean = redoStack.isNotEmpty()

    fun reset(initial: GameState) {
        undoStack.clear()
        redoStack.clear()
        undoStack.add(initial)
    }

    fun clear() {
        undoStack.clear()
        redoStack.clear()
    }

    fun getUndoStack(): List<GameState> = undoStack
    fun getRedoStack(): List<GameState> = redoStack
}
