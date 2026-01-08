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

package gr.tonygnk.sudoku.core.algorithm

import gr.tonygnk.sudoku.core.types.*

class QQWingOptions {
    // defaults for options
    var needNow = false
    var printPuzzle = false
    var printSolution = false
    var printHistory = false
    var printInstructions = false
    var timer = false
    var countSolutions = false
    var action = Action.NONE
    var logHistory = false
    var printStyle = PrintStyle.READABLE
    var numberToGenerate = 1
    var printStats = false
    var gameDifficulty = GameDifficulty.Unspecified
    var gameType = GameType.Unspecified
    var symmetry = Symmetry.NONE
    var threads = Runtime.getRuntime().availableProcessors()
}
