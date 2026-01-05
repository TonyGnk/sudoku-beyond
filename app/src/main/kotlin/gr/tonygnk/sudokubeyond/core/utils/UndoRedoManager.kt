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

package gr.tonygnk.sudokubeyond.core.utils

import gr.tonygnk.sudokubeyond.core.Cell
import gr.tonygnk.sudokubeyond.core.Note

class UndoRedoManager(private val initialState: GameState) {
    private var states: MutableList<GameState> = mutableListOf(initialState)
    private var currentState = 0

    /**
     * Adds a new game state to the history of states.
     * If the current state changes, all later states are removed.
     *
     * @param gameState The new game state to add.
     */
    fun addState(gameState: GameState) {
        if (currentState in states.indices && gameState == states[currentState]) {
            return
        }

        if (currentState < states.size - 1) {
            states = states.subList(0, currentState + 1).toMutableList()
        }

        states.add(gameState)
        currentState = states.size - 1
    }

    fun undo(): GameState {
        return if (canUndo()) {
            currentState -= 1
            states[currentState]
        } else {
            initialState
        }
    }

    fun redo(): GameState? {
        return if (canRedo()) {
            currentState += 1
            states[currentState]
        } else {
            null
        }
    }

    fun canRedo() = currentState < states.size - 1
    fun canUndo() = currentState > 0 && states.isNotEmpty()

    fun count() = states.count()
    fun clear() = states.clear()
}

data class GameState(
    val board: List<List<Cell>>,
    val notes: List<Note>
)