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

package gr.tonygnk.sudokubeyond.ui.game

import android.content.res.Configuration
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.tonygnk.sudoku.core.hint.AdvancedHintData
import gr.tonygnk.sudoku.core.model.Cell
import gr.tonygnk.sudoku.core.types.GameType
import gr.tonygnk.sudoku.core.utils.SudokuParser
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.PreferencesConstants
import gr.tonygnk.sudokubeyond.extensions.resName
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.ui.components.AdvancedHintContainer
import gr.tonygnk.sudokubeyond.ui.components.board.Board
import gr.tonygnk.sudokubeyond.ui.game.components.DefaultGameKeyboard
import gr.tonygnk.sudokubeyond.ui.game.components.GameMenu
import gr.tonygnk.sudokubeyond.ui.game.components.NotesMenu
import gr.tonygnk.sudokubeyond.ui.game.components.SquareGameKeyboard
import gr.tonygnk.sudokubeyond.ui.game.components.ToolBarItem
import gr.tonygnk.sudokubeyond.ui.game.components.ToolbarItem
import gr.tonygnk.sudokubeyond.ui.game.components.UndoRedoMenu
import gr.tonygnk.sudokubeyond.ui.onboarding.FirstGameDialog
import gr.tonygnk.sudokubeyond.ui.util.ReverseArrangement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    bloc: GameBloc,
    navigate: (MainActivityBloc.PagesConfig) -> Unit,
    finish: () -> Unit,
) {
    val localView = LocalView.current // vibration
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val firstGame by bloc.firstGame.collectAsStateWithLifecycle(initialValue = false)
    val resetTimer by bloc.resetTimerOnRestart.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_GAME_RESET_TIMER)
    val mistakesLimit by bloc.mistakesLimit.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_MISTAKES_LIMIT
    )
    val errorHighlight by bloc.mistakesMethod.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES)
    val keepScreenOn by bloc.keepScreenOn.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_KEEP_SCREEN_ON)
    val remainingUse by bloc.remainingUse.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_REMAINING_USES)
    val highlightIdentical by bloc.identicalHighlight.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_HIGHLIGHT_IDENTICAL
    )
    val positionLines by bloc.positionLines.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_POSITION_LINES
    )
    val crossHighlight by bloc.crossHighlight.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_BOARD_CROSS_HIGHLIGHT
    )
    val funKeyboardOverNum by bloc.funKeyboardOverNum.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_FUN_KEYBOARD_OVER_NUM
    )

    val fontSizeFactor by bloc.fontSize.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_FONT_SIZE_FACTOR)
    val fontSizeValue by remember(fontSizeFactor, bloc.gameType) {
        mutableStateOf(
            bloc.getFontSize(factor = fontSizeFactor)
        )
    }
    val advancedHintEnabled by bloc.advancedHintEnabled.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_ADVANCED_HINT
    )
    val advancedHintMode by bloc.advancedHintMode.collectAsStateWithLifecycle(false)
    val advancedHintData by bloc.advancedHintData.collectAsStateWithLifecycle(null)
    if (keepScreenOn) {
        KeepScreenOn()
    }

    if (firstGame) {
        bloc.pauseTimer()
        FirstGameDialog(
            onFinished = {
                bloc.setFirstGameFalse()
                bloc.startTimer()
            }
        )
    }

    var restartButtonAngleState by remember { mutableFloatStateOf(0f) }
    val restartButtonAnimation: Float by animateFloatAsState(
        targetValue = restartButtonAngleState,
        animationSpec = tween(durationMillis = 250), label = "restartButtonAnimation"
    )

    val boardBlur by animateDpAsState(
        targetValue = if (bloc.gamePlaying || bloc.endGame) 0.dp else 10.dp,
        label = "Game board blur"
    )
    val boardScale by animateFloatAsState(
        targetValue = if (bloc.gamePlaying || bloc.endGame) 1f else 0.90f,
        label = "Game board scale"
    )

    var renderNotes by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = finish) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    AnimatedVisibility(visible = bloc.endGame && (bloc.mistakesCount >= PreferencesConstants.MISTAKES_LIMIT || bloc.giveUp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledTonalButton(
                                onClick = { bloc.showSolution = !bloc.showSolution }
                            ) {
                                AnimatedContent(
                                    if (bloc.showSolution) stringResource(R.string.action_show_mine_sudoku)
                                    else stringResource(R.string.action_show_solution),
                                    label = "Show solution/mine button"
                                ) {
                                    Text(it)
                                }
                            }
                        }
                    }

                    AnimatedVisibility(visible = !bloc.endGame) {
                        val rotationAngle by animateFloatAsState(
                            targetValue = if (bloc.gamePlaying) 0f else 360f,
                            label = "Play/Pause game icon rotation"
                        )
                        IconButton(onClick = {
                            if (!bloc.gamePlaying) bloc.startTimer() else bloc.pauseTimer()
                            bloc.currCell = Cell(-1, -1, 0)
                        }) {
                            Icon(
                                modifier = Modifier.rotate(rotationAngle),
                                painter = painterResource(
                                    if (bloc.gamePlaying) {
                                        R.drawable.ic_round_pause_24
                                    } else {
                                        R.drawable.ic_round_play_24
                                    }
                                ),
                                contentDescription = null
                            )
                        }
                    }

                    AnimatedVisibility(visible = !bloc.endGame) {
                        IconButton(onClick = { bloc.restartDialog = true }) {
                            Icon(
                                modifier = Modifier.rotate(restartButtonAnimation),
                                painter = painterResource(R.drawable.ic_round_replay_24),
                                contentDescription = null
                            )
                        }
                    }
                    AnimatedVisibility(visible = !bloc.endGame) {
                        Box {
                            IconButton(onClick = { bloc.showMenu = !bloc.showMenu }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                            GameMenu(
                                expanded = bloc.showMenu,
                                onDismiss = { bloc.showMenu = false },
                                onGiveUpClick = {
                                    bloc.pauseTimer()
                                    bloc.giveUpDialog = true
                                },
                                onSettingsClick = {
//                                 TODO   navigator.navigate(
//                                        SettingsCategoriesScreenDestination(
//                                            launchedFromGame = true
//                                        )
//                                    )
                                    bloc.showMenu = false
                                },
                                onExportClick = {
                                    val stringBoard = SudokuParser().boardToString(
                                        bloc.gameBoard,
                                        emptySeparator = '.'
                                    )
                                    clipboardManager.setText(
                                        AnnotatedString(
                                            stringBoard.uppercase()
                                        )
                                    )

                                    if (SDK_INT < 33) {
                                        Toast.makeText(
                                            context,
                                            R.string.export_string_state_copied,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { scaffoldPaddings ->
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .padding(scaffoldPaddings)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.5f)
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        AnimatedVisibility(
                            visible = !bloc.gamePlaying && !bloc.endGame,
                            enter = expandVertically(clip = false) + fadeIn(),
                            exit = shrinkVertically(clip = false) + fadeOut()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayCircle,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .shadow(12.dp)
                            )
                        }
                    }
                    Board(
                        modifier = Modifier
                            .blur(boardBlur)
                            .scale(boardScale, boardScale),
                        board = if (!bloc.showSolution) bloc.gameBoard else bloc.solvedBoard,
                        size = bloc.size,
                        mainTextSize = fontSizeValue,
                        autoFontSize = fontSizeFactor == 0,
                        notes = bloc.notes,
                        selectedCell = bloc.currCell,
                        onClick = { cell ->
                            bloc.processInput(
                                cell = cell,
                                remainingUse = remainingUse,
                            )
                            if (!bloc.gamePlaying) {
                                localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                                bloc.startTimer()
                            }
                        },
                        onLongClick = { cell ->
                            if (bloc.processInput(cell, remainingUse, longTap = true)) {
                                localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                            }
                        },
                        identicalNumbersHighlight = highlightIdentical,
                        errorsHighlight = errorHighlight != 0,
                        positionLines = positionLines,
                        notesToHighlight = if (bloc.digitFirstNumber > 0) {
                            bloc.notes.filter { it.value == bloc.digitFirstNumber }
                        } else {
                            emptyList()
                        },
                        enabled = bloc.gamePlaying && !bloc.endGame,
                        questions = !(bloc.gamePlaying || bloc.endGame) && SDK_INT < Build.VERSION_CODES.R,
                        renderNotes = renderNotes && !bloc.showSolution,
                        zoomable = bloc.gameType == GameType.Default12x12 || bloc.gameType == GameType.Killer12x12,
                        crossHighlight = crossHighlight,
                        cages = bloc.cages,
                        cellsToHighlight = if (advancedHintMode && advancedHintData != null) advancedHintData!!.helpCells + advancedHintData!!.targetCell else null
                    )
                }

                AnimatedContent(advancedHintMode) { targetState ->
                    if (targetState) {
                        advancedHintData?.let { hintData ->
                            AdvancedHintContainer(
                                advancedHintData = hintData,
                                onApplyClick = {
                                    bloc.applyAdvancedHint()
                                },
                                onBackClick = {
                                    bloc.cancelAdvancedHint()
                                },
                                onSettingsClick = {
//                                  TODO  navigator.navigate(
//                                        SettingsAdvancedHintScreenDestination
//                                    )
                                }
                            )
                        }
                        if (advancedHintData == null) {
                            AdvancedHintContainer(
                                advancedHintData = AdvancedHintData(
                                    titleKey = stringResource(R.string.advanced_hint_no_hint_title),
                                    textKeyWithArg = Pair(
                                        stringResource(R.string.advanced_hint_no_hint),
                                        emptyList()
                                    ),
                                    targetCell = Cell(-1, -1, 0),
                                    helpCells = emptyList()
                                ),
                                onApplyClick = null,
                                onBackClick = {
                                    bloc.cancelAdvancedHint()
                                },
                                onSettingsClick = {
//                                   TODO navigator.navigate(
//                                        SettingsAdvancedHintScreenDestination
//                                    )
                                }
                            )
                        }
                    } else {
                        AnimatedContent(!bloc.endGame, label = "") { contentState ->
                            if (contentState) {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .padding(bottom = 8.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    AnimatedVisibility(visible = !bloc.endGame) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            TopBoardSection(stringResource(bloc.gameDifficulty.resName))

                                            if (mistakesLimit && errorHighlight != 0) {
                                                TopBoardSection(
                                                    stringResource(
                                                        R.string.mistakes_number_out_of,
                                                        bloc.mistakesCount,
                                                        3
                                                    )
                                                )
                                            }

                                            val timerEnabled by bloc.timerEnabled.collectAsStateWithLifecycle(
                                                initialValue = PreferencesConstants.DEFAULT_SHOW_TIMER
                                            )
                                            AnimatedVisibility(visible = timerEnabled || bloc.endGame) {
                                                TopBoardSection(bloc.timeText)
                                            }
                                        }
                                    }
                                    Column(
                                        verticalArrangement = if (funKeyboardOverNum) ReverseArrangement else Arrangement.Top
                                    ) {
                                        SquareGameKeyboard(
                                            size = bloc.size,
                                            remainingUses = if (remainingUse) bloc.remainingUsesList else null,
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
                                            Box(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                UndoRedoMenu(
                                                    expanded = bloc.showUndoRedoMenu,
                                                    onDismiss = {
                                                        bloc.showUndoRedoMenu = false
                                                    },
                                                    onRedoClick = {
                                                        bloc.toolbarClick(
                                                            ToolBarItem.Redo
                                                        )
                                                    }
                                                )
                                                ToolbarItem(
                                                    painter = painterResource(R.drawable.ic_round_undo_24),
                                                    onClick = { bloc.toolbarClick(ToolBarItem.Undo) },
                                                    onLongClick = {
                                                        bloc.showUndoRedoMenu = true
                                                    }
                                                )

                                            }
                                            val hintsDisabled by bloc.disableHints.collectAsStateWithLifecycle(
                                                initialValue = PreferencesConstants.DEFAULT_HINTS_DISABLED
                                            )
                                            if (!hintsDisabled) {
                                                ToolbarItem(
                                                    modifier = Modifier.weight(1f),
                                                    painter = painterResource(R.drawable.ic_lightbulb_stars_24),
                                                    enabled = bloc.currCell.row >= 0 && bloc.currCell.col >= 0,
                                                    onClick = { bloc.toolbarClick(ToolBarItem.Hint) }
                                                )
                                            }

                                            Box(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                NotesMenu(
                                                    expanded = bloc.showNotesMenu,
                                                    onDismiss = { bloc.showNotesMenu = false },
                                                    onComputeNotesClick = { bloc.computeNotes() },
                                                    onClearNotesClick = { bloc.clearNotes() },
                                                    renderNotes = renderNotes,
                                                    onRenderNotesClick = {
                                                        renderNotes = !renderNotes
                                                    }
                                                )
                                                ToolbarItem(
                                                    painter = painterResource(R.drawable.ic_round_edit_24),
                                                    toggled = bloc.notesToggled,
                                                    onClick = { bloc.toolbarClick(ToolBarItem.Note) },
                                                    onLongClick = {
                                                        if (bloc.gamePlaying) {
                                                            localView.performHapticFeedback(
                                                                HapticFeedbackConstants.VIRTUAL_KEY
                                                            )
                                                            bloc.showNotesMenu = true
                                                        }
                                                    }
                                                )

                                            }
                                            ToolbarItem(
                                                modifier = Modifier.weight(1f),
                                                painter = painterResource(R.drawable.ic_eraser_24),
                                                toggled = bloc.eraseButtonToggled,
                                                onClick = {
                                                    bloc.toolbarClick(ToolBarItem.Remove)
                                                },
                                                onLongClick = {
                                                    if (bloc.gamePlaying) {
                                                        localView.performHapticFeedback(
                                                            HapticFeedbackConstants.VIRTUAL_KEY
                                                        )
                                                        bloc.toggleEraseButton()
                                                    }
                                                }
                                            )
                                            if (advancedHintEnabled) {
                                                ToolbarItem(
                                                    modifier = Modifier.weight(1f),
                                                    painter = rememberVectorPainter(Icons.Rounded.AutoAwesome),
                                                    onClick = {
                                                        if (bloc.gamePlaying) {
                                                            bloc.getAdvancedHint()
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Game completed section
                                val allRecords by bloc.allRecords.collectAsStateWithLifecycle(
                                    initialValue = emptyList()
                                )
                                AfterGameStats(
                                    modifier = Modifier.fillMaxWidth(),
                                    difficulty = bloc.gameDifficulty,
                                    type = bloc.gameType,
                                    hintsUsed = bloc.hintsUsed,
                                    mistakesMade = bloc.mistakesMade,
                                    mistakesLimit = mistakesLimit,
                                    mistakesLimitCount = bloc.mistakesCount,
                                    giveUp = bloc.giveUp,
                                    notesTaken = bloc.notesTaken,
                                    records = allRecords,
                                    timeText = bloc.timeText
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(scaffoldPaddings)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                AnimatedVisibility(visible = !bloc.endGame) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TopBoardSection(stringResource(bloc.gameDifficulty.resName))

                        if (mistakesLimit && errorHighlight != 0) {
                            TopBoardSection(
                                stringResource(
                                    R.string.mistakes_number_out_of,
                                    bloc.mistakesCount,
                                    3
                                )
                            )
                        }

                        val timerEnabled by bloc.timerEnabled.collectAsStateWithLifecycle(
                            initialValue = PreferencesConstants.DEFAULT_SHOW_TIMER
                        )
                        AnimatedVisibility(visible = timerEnabled || bloc.endGame) {
                            TopBoardSection(bloc.timeText)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        AnimatedVisibility(
                            visible = !bloc.gamePlaying && !bloc.endGame,
                            enter = expandVertically(clip = false) + fadeIn(),
                            exit = shrinkVertically(clip = false) + fadeOut()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayCircle,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .shadow(12.dp)
                            )
                        }
                    }
                    Board(
                        modifier = Modifier
                            .blur(boardBlur)
                            .scale(boardScale, boardScale),
                        board = if (!bloc.showSolution) bloc.gameBoard else bloc.solvedBoard,
                        size = bloc.size,
                        mainTextSize = fontSizeValue,
                        autoFontSize = fontSizeFactor == 0,
                        notes = bloc.notes,
                        selectedCell = bloc.currCell,
                        onClick = { cell ->
                            bloc.processInput(
                                cell = cell,
                                remainingUse = remainingUse,
                            )
                            if (!bloc.gamePlaying) {
                                localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                                bloc.startTimer()
                            }
                        },
                        onLongClick = { cell ->
                            if (bloc.processInput(cell, remainingUse, longTap = true)) {
                                localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                            }
                        },
                        identicalNumbersHighlight = highlightIdentical,
                        errorsHighlight = errorHighlight != 0,
                        positionLines = positionLines,
                        notesToHighlight = if (bloc.digitFirstNumber > 0) {
                            bloc.notes.filter { it.value == bloc.digitFirstNumber }
                        } else {
                            emptyList()
                        },
                        enabled = bloc.gamePlaying && !bloc.endGame,
                        questions = !(bloc.gamePlaying || bloc.endGame) && SDK_INT < Build.VERSION_CODES.R,
                        renderNotes = renderNotes && !bloc.showSolution,
                        zoomable = bloc.gameType == GameType.Default12x12 || bloc.gameType == GameType.Killer12x12,
                        crossHighlight = crossHighlight,
                        cages = bloc.cages,
                        cellsToHighlight = if (advancedHintMode && advancedHintData != null) advancedHintData!!.helpCells + advancedHintData!!.targetCell else null
                    )
                }

                AnimatedContent(advancedHintMode) { targetState ->
                    if (targetState) {
                        advancedHintData?.let { hintData ->
                            AdvancedHintContainer(
                                advancedHintData = hintData,
                                onApplyClick = {
                                    bloc.applyAdvancedHint()
                                },
                                onBackClick = {
                                    bloc.cancelAdvancedHint()
                                },
                                onSettingsClick = {
//                                  TODO  navigator.navigate(
//                                        SettingsAdvancedHintScreenDestination
//                                    )
                                }
                            )
                        }
                        if (advancedHintData == null) {
                            AdvancedHintContainer(
                                advancedHintData = AdvancedHintData(
                                    titleKey = stringResource(R.string.advanced_hint_no_hint_title),
                                    textKeyWithArg = Pair(
                                        stringResource(R.string.advanced_hint_no_hint),
                                        emptyList()
                                    ),
                                    targetCell = Cell(-1, -1, 0),
                                    helpCells = emptyList()
                                ),
                                onApplyClick = null,
                                onBackClick = {
                                    bloc.cancelAdvancedHint()
                                },
                                onSettingsClick = {
//                                 TODO   navigator.navigate(
//                                        SettingsAdvancedHintScreenDestination
//                                    )
                                }
                            )
                        }
                    } else {
                        AnimatedContent(!bloc.endGame, label = "") { contentState ->
                            if (contentState) {
                                Column(
                                    verticalArrangement = if (funKeyboardOverNum) ReverseArrangement else Arrangement.Top
                                ) {
                                    DefaultGameKeyboard(
                                        size = bloc.size,
                                        remainingUses = if (remainingUse) bloc.remainingUsesList else null,
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
                                        Box(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            UndoRedoMenu(
                                                expanded = bloc.showUndoRedoMenu,
                                                onDismiss = { bloc.showUndoRedoMenu = false },
                                                onRedoClick = { bloc.toolbarClick(ToolBarItem.Redo) }
                                            )
                                            ToolbarItem(
                                                painter = painterResource(R.drawable.ic_round_undo_24),
                                                onClick = { bloc.toolbarClick(ToolBarItem.Undo) },
                                                onLongClick = { bloc.showUndoRedoMenu = true }
                                            )

                                        }
                                        val hintsDisabled by bloc.disableHints.collectAsStateWithLifecycle(
                                            initialValue = PreferencesConstants.DEFAULT_HINTS_DISABLED
                                        )
                                        if (!hintsDisabled) {
                                            ToolbarItem(
                                                modifier = Modifier.weight(1f),
                                                painter = painterResource(R.drawable.ic_lightbulb_stars_24),
                                                enabled = bloc.currCell.row >= 0 && bloc.currCell.col >= 0,
                                                onClick = { bloc.toolbarClick(ToolBarItem.Hint) }
                                            )
                                        }

                                        Box(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            NotesMenu(
                                                expanded = bloc.showNotesMenu,
                                                onDismiss = { bloc.showNotesMenu = false },
                                                onComputeNotesClick = { bloc.computeNotes() },
                                                onClearNotesClick = { bloc.clearNotes() },
                                                renderNotes = renderNotes,
                                                onRenderNotesClick = { renderNotes = !renderNotes }
                                            )
                                            ToolbarItem(
                                                painter = painterResource(R.drawable.ic_round_edit_24),
                                                toggled = bloc.notesToggled,
                                                onClick = { bloc.toolbarClick(ToolBarItem.Note) },
                                                onLongClick = {
                                                    if (bloc.gamePlaying) {
                                                        localView.performHapticFeedback(
                                                            HapticFeedbackConstants.VIRTUAL_KEY
                                                        )
                                                        bloc.showNotesMenu = true
                                                    }
                                                }
                                            )

                                        }
                                        ToolbarItem(
                                            modifier = Modifier.weight(1f),
                                            painter = painterResource(R.drawable.ic_eraser_24),
                                            toggled = bloc.eraseButtonToggled,
                                            onClick = {
                                                bloc.toolbarClick(ToolBarItem.Remove)
                                            },
                                            onLongClick = {
                                                if (bloc.gamePlaying) {
                                                    localView.performHapticFeedback(
                                                        HapticFeedbackConstants.VIRTUAL_KEY
                                                    )
                                                    bloc.toggleEraseButton()
                                                }
                                            }
                                        )
                                        if (advancedHintEnabled) {
                                            ToolbarItem(
                                                modifier = Modifier.weight(1f),
                                                painter = rememberVectorPainter(Icons.Rounded.AutoAwesome),
                                                onClick = {
                                                    if (bloc.gamePlaying) {
                                                        bloc.getAdvancedHint()
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Game completed section
                                val allRecords by bloc.allRecords.collectAsStateWithLifecycle(
                                    initialValue = emptyList()
                                )
                                AfterGameStats(
                                    modifier = Modifier.fillMaxWidth(),
                                    difficulty = bloc.gameDifficulty,
                                    type = bloc.gameType,
                                    hintsUsed = bloc.hintsUsed,
                                    mistakesMade = bloc.mistakesMade,
                                    mistakesLimit = mistakesLimit,
                                    mistakesLimitCount = bloc.mistakesCount,
                                    giveUp = bloc.giveUp,
                                    notesTaken = bloc.notesTaken,
                                    records = allRecords,
                                    timeText = bloc.timeText
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // dialogs
    if (bloc.restartDialog) {
        bloc.pauseTimer()
        AlertDialog(
            title = { Text(stringResource(R.string.action_reset_game)) },
            text = { Text(stringResource(R.string.reset_game_text)) },
            dismissButton = {
                TextButton(onClick = {
                    bloc.restartDialog = false
                    bloc.startTimer()
                }) {
                    Text(stringResource(R.string.dialog_no))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    restartButtonAngleState -= 360
                    bloc.resetGame(resetTimer)
                    bloc.restartDialog = false
                    bloc.startTimer()
                }) {
                    Text(stringResource(R.string.dialog_yes))
                }
            },
            onDismissRequest = {
                bloc.restartDialog = false
                bloc.startTimer()
            }
        )
    } else if (bloc.giveUpDialog) {
        bloc.pauseTimer()
        AlertDialog(
            title = { Text(stringResource(R.string.action_give_up)) },
            text = { Text(stringResource(R.string.give_up_text)) },
            dismissButton = {
                TextButton(onClick = {
                    bloc.giveUpDialog = false
                    bloc.startTimer()
                }) {
                    Text(stringResource(R.string.dialog_no))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    bloc.giveUp()
                    bloc.giveUpDialog = false
                    bloc.pauseTimer()
                }) {
                    Text(stringResource(R.string.dialog_yes))
                }
            },
            onDismissRequest = {
                bloc.giveUpDialog = false
                bloc.startTimer()
            },
        )
    }

    LaunchedEffect(bloc.mistakesMethod) {
        bloc.checkMistakesAll()
    }

    LaunchedEffect(Unit) {
        if (!bloc.endGame && !bloc.gameCompleted) {
            bloc.startTimer()
        }
    }

    LaunchedEffect(bloc.gameCompleted) {
        if (bloc.gameCompleted) {
            bloc.onGameComplete()
        }
    }


    // so that the timer doesn't run in the background
    // https://stackoverflow.com/questions/66546962/jetpack-compose-how-do-i-refresh-a-screen-when-app-returns-to-foreground/66807899#66807899
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if (bloc.gamePlaying) bloc.startTimer()
            }

            Lifecycle.Event.ON_PAUSE -> {
                bloc.pauseTimer()
                bloc.currCell = Cell(-1, -1, 0)
            }

            Lifecycle.Event.ON_DESTROY -> bloc.pauseTimer()
            else -> {}
        }
    }
}


@Composable
fun TopBoardSection(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun KeepScreenOn() = AndroidView({ View(it).apply { keepScreenOn = true } })