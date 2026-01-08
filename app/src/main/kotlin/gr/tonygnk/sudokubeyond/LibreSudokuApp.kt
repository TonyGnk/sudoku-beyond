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

package gr.tonygnk.sudokubeyond

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import gr.tonygnk.sudoku.core.utils.globalLogger
import gr.tonygnk.sudokubeyond.di.AppModule
import gr.tonygnk.sudokubeyond.di.AppModuleImpl
import gr.tonygnk.sudokubeyond.di.AppWorkerFactory
import gr.tonygnk.sudokubeyond.utils.AndroidLogger

internal class LibreSudokuApp : Application(), Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(AppWorkerFactory())
            .build()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the logger for sudoku-core module
        globalLogger = AndroidLogger
        appModule = AppModuleImpl(this)
    }

    companion object {
        lateinit var appModule: AppModule
            private set
    }
}