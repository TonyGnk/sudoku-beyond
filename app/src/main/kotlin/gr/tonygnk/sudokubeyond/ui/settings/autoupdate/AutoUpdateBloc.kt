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

package gr.tonygnk.sudokubeyond.ui.settings.autoupdate

import android.util.Log
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudokubeyond.BuildConfig
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.domain.usecase.update.UpdateCheckUseCase
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.updates.ReleaseBrief
import gr.tonygnk.sudokubeyond.updates.UpdateSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalDecomposeApi::class)
class AutoUpdateBloc(
    blocContext: BlocContext,
    private val appSettingsManager: AppSettingsManager,
    private val updateCheckUseCase: UpdateCheckUseCase,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {

    private val scope = lifecycle.coroutineScope

    val updateChannel = appSettingsManager.autoUpdateChannel

    private val _latestRelease = MutableStateFlow<ReleaseBrief?>(null)
    val latestRelease: StateFlow<ReleaseBrief?> = _latestRelease.asStateFlow()

    private val _checkingForUpdates = MutableStateFlow(false)
    val checkingForUpdates: StateFlow<Boolean> = _checkingForUpdates.asStateFlow()

    private val _checkingForUpdatesError = MutableStateFlow(false)
    val checkingForUpdatesError: StateFlow<Boolean> = _checkingForUpdatesError.asStateFlow()

    init {
        scope.launch {
            updateChannel.collect { channel ->
                if (channel != UpdateChannel.Disabled && UpdateSystem.canUpdate) {
                    checkForUpdates(
                        allowBetas = channel == UpdateChannel.Beta
                    )
                }
            }
        }
    }

    fun checkForUpdates(allowBetas: Boolean) {
        if (!UpdateSystem.canUpdate) return

        scope.launch {
            _checkingForUpdates.value = true
            _latestRelease.value = null
            _checkingForUpdatesError.value = false

            try {
                val result = updateCheckUseCase(
                    currentVersion = BuildConfig.VERSION_NAME,
                    allowBetas = allowBetas,
                    forceRefresh = true
                )

                when (result) {
                    is UpdateCheckUseCase.Result.Success -> {
                        _latestRelease.value = result.release
                        _checkingForUpdates.value = false
                    }
                    is UpdateCheckUseCase.Result.Error -> {
                        Log.e(TAG, "Failed to check for updates", result.exception)
                        _checkingForUpdates.value = false
                        _checkingForUpdatesError.value = true
                    }
                    is UpdateCheckUseCase.Result.CachedError -> {
                        // This shouldn't happen with forceRefresh=true, but handle it
                        _checkingForUpdates.value = false
                        _checkingForUpdatesError.value = true
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to check for updates", e)
                _checkingForUpdates.value = false
                _checkingForUpdatesError.value = true
            }
        }
    }

    fun updateAutoUpdateChannel(channel: UpdateChannel) {
        scope.launch(Dispatchers.IO) {
            appSettingsManager.setAutoUpdateChannel(channel)
            appSettingsManager.clearCachedUpdateRelease()
        }
    }

    companion object {
        private const val TAG = "AutoUpdateBloc"
        operator fun invoke(blocContext: BlocContext) = AutoUpdateBloc(
            blocContext = blocContext,
            appSettingsManager = LibreSudokuApp.appModule.appSettingsManager,
            updateCheckUseCase = UpdateCheckUseCase(
                appSettingsManager = LibreSudokuApp.appModule.appSettingsManager,
            ),
        )
    }
}