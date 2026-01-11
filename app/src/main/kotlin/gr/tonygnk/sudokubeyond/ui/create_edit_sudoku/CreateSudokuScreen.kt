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

package gr.tonygnk.sudokubeyond.ui.create_edit_sudoku

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.tonygnk.sudoku.core.types.GameDifficulty
import gr.tonygnk.sudoku.core.types.GameDifficulty.Challenge
import gr.tonygnk.sudoku.core.types.GameDifficulty.Custom
import gr.tonygnk.sudoku.core.types.GameDifficulty.Easy
import gr.tonygnk.sudoku.core.types.GameDifficulty.Hard
import gr.tonygnk.sudoku.core.types.GameDifficulty.Moderate
import gr.tonygnk.sudoku.core.types.GameType
import gr.tonygnk.sudoku.core.types.GameType.Default12x12
import gr.tonygnk.sudoku.core.types.GameType.Default6x6
import gr.tonygnk.sudoku.core.types.GameType.Default9x9
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.PreferencesConstants
import gr.tonygnk.sudokubeyond.extensions.resName
import gr.tonygnk.sudokubeyond.ui.components.board.Board
import gr.tonygnk.sudokubeyond.ui.game.components.DefaultGameKeyboard
import gr.tonygnk.sudokubeyond.ui.game.components.ToolBarItem
import gr.tonygnk.sudokubeyond.ui.game.components.ToolbarItem
import gr.tonygnk.sudokubeyond.ui.util.ReverseArrangement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSudokuScreen(
    bloc: CreateSudokuBloc,
    finish: () -> Unit,
) {
    var importStringDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (bloc.gameUid == -1L)
                            stringResource(R.string.create_sudoku_title)
                        else
                            stringResource(R.string.edit_sudoku)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = finish) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    var showMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = MaterialTheme.shapes.large)) {
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(stringResource(R.string.create_set_from_string))
                                    },
                                    onClick = {
                                        importStringDialog = true
                                        showMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            val highlightIdentical by bloc.highlightIdentical.collectAsState(initial = PreferencesConstants.DEFAULT_HIGHLIGHT_IDENTICAL)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Box {
                        var difficultyMenu by remember { mutableStateOf(false) }
                        val dropDownIconRotation by animateFloatAsState(if (difficultyMenu) 180f else 0f)
                        TextButton(onClick = { difficultyMenu = !difficultyMenu }) {
                            Text(stringResource(bloc.gameDifficulty.resName))
                            Icon(
                                modifier = Modifier.rotate(dropDownIconRotation),
                                imageVector = Icons.Rounded.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                        DifficultyMenu(
                            expanded = difficultyMenu,
                            onDismissRequest = { difficultyMenu = false },
                            onClick = {
                                bloc.changeGameDifficulty(it)
                            }
                        )
                    }
                    // allow changing a game type only when creating a new sudoku
                    if (bloc.gameUid == -1L) {
                        Box {
                            var gameTypeMenuExpanded by remember { mutableStateOf(false) }
                            val dropDownIconRotation by animateFloatAsState(if (gameTypeMenuExpanded) 180f else 0f)
                            TextButton(onClick = { gameTypeMenuExpanded = !gameTypeMenuExpanded }) {
                                Text(stringResource(bloc.gameType.resName))
                                Icon(
                                    modifier = Modifier.rotate(dropDownIconRotation),
                                    imageVector = Icons.Rounded.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                            GameTypeMenu(
                                expanded = gameTypeMenuExpanded,
                                onDismissRequest = { gameTypeMenuExpanded = false },
                                onClick = {
                                    bloc.changeGameType(it)
                                }
                            )
                        }
                    }
                }
                FilledTonalButton(
                    enabled = !bloc.gameBoard.flatten().all { it.value == 0 },
                    onClick = {
                        if (bloc.saveGame()) {
                            finish()
                        }
                    }) {
                    Text(stringResource(R.string.action_save))
                }
            }

            val fontSizeFactor by bloc.fontSize.collectAsState(initial = PreferencesConstants.DEFAULT_FONT_SIZE_FACTOR)
            val fontSizeValue by remember(fontSizeFactor, bloc.gameType) {
                mutableStateOf(
                    bloc.getFontSize(factor = fontSizeFactor)
                )
            }

            val positionLines by bloc.positionLines.collectAsState(initial = PreferencesConstants.DEFAULT_POSITION_LINES)
            val crossHighlight by bloc.crossHighlight.collectAsState(initial = PreferencesConstants.DEFAULT_BOARD_CROSS_HIGHLIGHT)
            Board(
                modifier = Modifier.padding(vertical = 12.dp),
                size = bloc.gameType.size,
                mainTextSize = fontSizeValue,
                autoFontSize = fontSizeFactor == 0,
                board = bloc.gameBoard,
                selectedCell = bloc.currCell,
                onClick = { cell ->
                    bloc.processInput(cell = cell)
                },
                identicalNumbersHighlight = highlightIdentical,
                positionLines = positionLines,
                crossHighlight = crossHighlight
            )

            val funKeyboardOverNum by bloc.funKeyboardOverNum.collectAsStateWithLifecycle(
                initialValue = PreferencesConstants.DEFAULT_FUN_KEYBOARD_OVER_NUM
            )

            Column(
                verticalArrangement = if (funKeyboardOverNum) ReverseArrangement else Arrangement.Top
            ) {
                DefaultGameKeyboard(
                    size = bloc.gameType.size,
                    remainingUses = null,
                    onClick = {
                        bloc.processInputKeyboard(number = it)
                    },
                    onLongClick = {
                        bloc.processInputKeyboard(
                            number = it,
                            longTap = true
                        )
                    },
                    selected = bloc.digitFirstNumber
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    ToolbarItem(
                        modifier = Modifier.weight(0.5f),
                        painter = painterResource(R.drawable.ic_round_undo_24),
                        onClick = { bloc.toolbarClick(ToolBarItem.Undo) }
                    )

                    ToolbarItem(
                        modifier = Modifier.weight(0.5f),
                        painter = rememberVectorPainter(Icons.AutoMirrored.Rounded.Redo),
                        onClick = { bloc.toolbarClick(ToolBarItem.Redo) }
                    )

                    ToolbarItem(
                        modifier = Modifier.weight(1f),
                        painter = painterResource(R.drawable.ic_eraser_24),
                        onClick = {
                            bloc.toolbarClick(ToolBarItem.Remove)
                        }
                    )
                }
            }

            if (importStringDialog) {
                ImportStringSudokuDialog(
                    textValue = bloc.importStringValue,
                    onTextChange = {
                        bloc.importStringValue = it
                        bloc.importTextFieldError = false
                    },
                    isError = bloc.importTextFieldError,
                    onConfirm = {
                        bloc.setFromString(bloc.importStringValue.trim()).also {
                            bloc.importTextFieldError = !it
                            if (it) {
                                importStringDialog = false
                                bloc.importStringValue = ""
                            }
                        }
                    },
                    onDismiss = { importStringDialog = false }
                )
            } else if (bloc.multipleSolutionsDialog) {
                AlertDialog(
                    title = { Text(stringResource(R.string.create_incorrect_puzzle)) },
                    text = {
                        Text(stringResource(R.string.multiple_solution_text))
                    },
                    onDismissRequest = { bloc.multipleSolutionsDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            bloc.multipleSolutionsDialog = false
                        }) {
                            Text(stringResource(R.string.dialog_ok))
                        }
                    }
                )
            } else if (bloc.noSolutionsDialog) {
                AlertDialog(
                    title = { Text(stringResource(R.string.create_incorrect_puzzle)) },
                    text = {
                        Text(stringResource(R.string.no_solution_text))
                    },
                    onDismissRequest = { bloc.noSolutionsDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            bloc.noSolutionsDialog = false
                        }) {
                            Text(stringResource(R.string.dialog_ok))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GameTypeMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    types: List<GameType> = listOf(Default9x9, Default6x6, Default12x12),
    onClick: (GameType) -> Unit,
) {
    MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = MaterialTheme.shapes.large)) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            types.forEach {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(it.resName))
                    },
                    onClick = {
                        onClick(it)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
private fun ImportStringSudokuDialog(
    textValue: String,
    onTextChange: (String) -> Unit,
    isError: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    AlertDialog(
        title = { Text(stringResource(R.string.create_set_from_string)) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.create_from_string_text))
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .focusRequester(focusRequester),
                    value = textValue,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onConfirm() }
                    ),
                    isError = isError,
                    onValueChange = onTextChange,
                    label = { Text(stringResource(R.string.create_from_string_hint)) }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.create_import_set))
            }
        }
    )
}

@Composable
fun DifficultyMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    difficulties: List<GameDifficulty> = listOf(Easy, Moderate, Hard, Challenge, Custom),
    onClick: (GameDifficulty) -> Unit,
) {
    MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = MaterialTheme.shapes.large)) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            difficulties.forEach {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(it.resName))
                    },
                    onClick = {
                        onClick(it)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}