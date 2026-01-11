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

package gr.tonygnk.sudokubeyond.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import gr.tonygnk.sudoku.core.model.Cell
import gr.tonygnk.sudoku.core.types.GameType
import gr.tonygnk.sudoku.core.utils.SudokuParser
import gr.tonygnk.sudokubeyond.LibreSudokuApp
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.BlocContext
import gr.tonygnk.sudokubeyond.data.datastore.AppSettingsManager
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.ui.components.board.Board
import gr.tonygnk.sudokubeyond.ui.util.getCurrentLocaleString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    bloc: WelcomeBloc,
    navigate: (MainActivityBloc.PagesConfig) -> Unit,
    removeWelcomeAndNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val currentLanguage by remember {
        mutableStateOf(
            getCurrentLocaleString(context)
        )
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(stringResource(R.string.intro_rules))
                    Board(
                        board = bloc.previewBoard,
                        size = 9,
                        selectedCell = bloc.selectedCell,
                        onClick = { cell -> bloc.selectedCell = cell }
                    )

                    Button(
                        onClick = {
                            bloc.setFirstLaunch()
                            removeWelcomeAndNavigateToHome()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(stringResource(R.string.action_start))
                    }

                    ItemRowBigIcon(
                        title = stringResource(R.string.pref_app_language),
                        icon = Icons.Rounded.Language,
                        subtitle = currentLanguage,
                        onClick = {
                            //TODO navigator.navigate(SettingsLanguageScreenDestination())
                        },
                    )
                    ItemRowBigIcon(
                        title = stringResource(R.string.onboard_restore_backup),
                        icon = Icons.Rounded.Restore,
                        subtitle = stringResource(R.string.onboard_restore_backup_description),
                        onClick = {
                            //TODO  navigator.navigate(BackupScreenDestination)
                        }
                    )
                    ItemRowBigIcon(
                        title = stringResource(R.string.settings_title),
                        icon = Icons.Rounded.Settings,
                        subtitle = stringResource(R.string.onboard_settings_description),
                        onClick = {
                            //TODO  navigator.navigate(SettingsCategoriesScreenDestination(false))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemRowBigIcon(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit = { },
    onClick: () -> Unit = { },
    subtitle: String? = null,
    shape: Shape = MaterialTheme.shapes.large,
    onLongClick: ((() -> Unit))? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    subtitleStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
    containerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
    iconBackground: Color = MaterialTheme.colorScheme.secondaryContainer,
    iconSize: Dp = 42.dp,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(containerColor)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier.background(
                        color = iconBackground,
                        shape = MaterialTheme.shapes.medium
                    )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(iconSize)
                            .padding(6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = title,
                        style = titleStyle
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = subtitleStyle,
                            color = LocalContentColor.current.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            trailing()
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
class WelcomeBloc(
    blocContext: BlocContext,
    private val settingsDataManager: AppSettingsManager,
) : MainActivityBloc.PagesBloc, BlocContext by blocContext {

    private val scope = lifecycle.coroutineScope

    var selectedCell by mutableStateOf(Cell(-1, -1, 0))

    // all heart shaped ❤
    val previewBoard = SudokuParser().parseBoard(
        board = listOf(
            "072000350340502018100030009800000003030000070050000020008000600000103000760050041",
            "017000230920608054400010009200000001060000020040000090002000800000503000390020047",
            "052000180480906023600020007500000008020000060030000090005000300000708000370060014",
            "025000860360208017700010003600000002040000090030000070006000100000507000490030058",
            "049000380280309056600050007300000002010000030070000090003000800000604000420080013",
            "071000420490802073300060009200000007060000090010000080007000900000703000130090068",
            "023000190150402086800050004700000008090000030080000010008000700000306000530070029",
            "097000280280706013300080007600000002040000060030000090001000400000105000860040051",
            "049000180160904023700010004200000008090000060080000050005000600000706000470020031"
        ).random(),
        gameType = GameType.Default9x9
    )

    fun setFirstLaunch(value: Boolean = false) {
        scope.launch(Dispatchers.IO) {
            settingsDataManager.setFirstLaunch(value)
        }
    }

    companion object Companion {
        operator fun invoke(blocContext: BlocContext) = WelcomeBloc(
            blocContext = blocContext,
            settingsDataManager = LibreSudokuApp.appModule.appSettingsManager
        )
    }
}