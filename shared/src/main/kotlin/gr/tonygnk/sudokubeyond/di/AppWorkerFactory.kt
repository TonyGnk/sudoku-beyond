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

package gr.tonygnk.sudokubeyond.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.data.backup.BackupWorker

internal class AppWorkerFactory() : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            BackupWorker::class.java.name -> BackupWorker(
                context = appContext,
                workerParameters = workerParameters,
                appSettingsManager = LibreSudokuApp.appModule.appSettingsManager,
                boardRepository = LibreSudokuApp.appModule.boardRepository,
                folderRepository = LibreSudokuApp.appModule.folderRepository,
                recordRepository = LibreSudokuApp.appModule.recordRepository,
                savedGameRepository = LibreSudokuApp.appModule.savedGameRepository,
                themeSettingsManager = LibreSudokuApp.appModule.themeSettingsManager
            )
            else -> null
        }
    }
}