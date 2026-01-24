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

package gr.tonygnk.sudokubeyond.ui.import_from_file

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudoku.core.types.GameDifficulty
import gr.tonygnk.sudoku.core.types.GameType
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.core.parser.GsudokuParser
import gr.tonygnk.sudokubeyond.core.parser.OpenSudokuParser
import gr.tonygnk.sudokubeyond.core.parser.SdmParser
import gr.tonygnk.sudokubeyond.data.database.model.Folder
import gr.tonygnk.sudokubeyond.data.database.model.SudokuBoard
import gr.tonygnk.sudokubeyond.domain.repository.BoardRepository
import gr.tonygnk.sudokubeyond.domain.usecase.folder.InsertFolderUseCase
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.ZonedDateTime

@OptIn(ExperimentalDecomposeApi::class)
class ImportFromFileBloc(
    blocContext: BlocContext,
    val fileUri: String?,
    folderUid: Long,
    val fromDeepLink: Boolean,
    private val insertFolderUseCase: InsertFolderUseCase,
    private val boardRepository: BoardRepository,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {
    
    private val scope = lifecycle.coroutineScope

    // uid of the folder where to add the imported sudoku.
    // If uid = -1, a new folder will be created (ask the user for the folder name)
    val folderUidState by mutableLongStateOf(folderUid)

    var isLoading by mutableStateOf(true)
    var isSaved by mutableStateOf(false)
    var isSaving by mutableStateOf(false)

    private val _sudokuListToImport = MutableStateFlow(emptyList<String>())
    val sudokuListToImport = _sudokuListToImport.asStateFlow()

    var difficultyForImport by mutableStateOf(GameDifficulty.Easy)

    private val _importingError = MutableStateFlow(false)
    val importError = _importingError.asStateFlow()

    fun readData(inputStream: InputStreamReader) {
        scope.launch(Dispatchers.Default) {
            var toImport = listOf<String>()
            try {
                BufferedReader(inputStream).use { bufferRead ->
                    val contentText = bufferRead.readText()

                    val parser = when {
                        contentText.contains("<opensudoku") -> OpenSudokuParser()

                        contentText.contains("<onegravitysudoku") -> GsudokuParser()

                        else -> SdmParser()
                    }
                    val result = parser.toBoards(contentText)
                    toImport = result.second
                    _importingError.emit(!result.first)
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _importingError.emit(true)
            } finally {
                withContext(Dispatchers.IO) {
                    inputStream.close()
                }
                if (!_importingError.value) {
                    _sudokuListToImport.emit(toImport.toList())
                }
            }
        }
    }

    fun saveImported(folderName: String? = null) {
        scope.launch {
            isSaving = true
            var folderUidToImport = folderUidState

            if (folderUidToImport == -1L) {
                folderUidToImport = insertFolderUseCase(
                    Folder(
                        uid = 0,
                        name = folderName ?: sudokuListToImport.value.size.toString(),
                        createdAt = ZonedDateTime.now()
                    )
                )
            }

            val sudokuBoardsToImport = mutableListOf<SudokuBoard>()
            sudokuListToImport.value.forEach {
                sudokuBoardsToImport.add(
                    SudokuBoard(
                        uid = 0,
                        initialBoard = it,
                        solvedBoard = "",
                        difficulty = difficultyForImport,
                        type = GameType.Default9x9,
                        folderId = folderUidToImport
                    )
                )
            }
            boardRepository.insert(sudokuBoardsToImport)
            _sudokuListToImport.emit(emptyList())
            isSaving = false
            isSaved = true
        }
    }

    fun setDifficulty(difficulty: GameDifficulty) {
        difficultyForImport = difficulty
    }

    companion object Companion {
        operator fun invoke(
            blocContext: BlocContext,
            fileUri: String?,
            folderUid: Long,
            fromDeepLink: Boolean,
        ) = ImportFromFileBloc(
            blocContext = blocContext,
            fileUri = fileUri,
            folderUid = folderUid,
            fromDeepLink = fromDeepLink,
            insertFolderUseCase = InsertFolderUseCase(LibreSudokuApp.appModule.folderRepository),
            boardRepository = LibreSudokuApp.appModule.boardRepository,
        )
    }
}