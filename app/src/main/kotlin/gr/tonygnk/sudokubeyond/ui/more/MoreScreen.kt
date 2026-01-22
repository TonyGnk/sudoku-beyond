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

package gr.tonygnk.sudokubeyond.ui.more

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.materialkolor.ktx.blend
import gr.tonygnk.sudokubeyond.BuildConfig
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.AboutConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.AutoUpdateConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.BackupConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.FoldersConfig
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc.PagesConfig.LearnConfig
import gr.tonygnk.sudokubeyond.ui.components.PreferenceRow
import gr.tonygnk.sudokubeyond.ui.settings.autoupdate.UpdateChannel
import gr.tonygnk.sudokubeyond.ui.theme.RoundedPolygonShape
import gr.tonygnk.sudokubeyond.updates.ReleaseBrief
import gr.tonygnk.sudokubeyond.updates.UpdateSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MoreScreen(
    bloc: MoreBloc,
    navigate: (MainActivityBloc.PagesConfig) -> Unit,
) {
    val autoUpdateChannel by bloc.updateChannel.collectAsStateWithLifecycle(UpdateChannel.Disabled)
    val updateDismissedName by bloc.updateDismissedName.collectAsStateWithLifecycle("")

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            PreferenceRow(
                title = stringResource(R.string.settings_title),
                drawableRes = R.drawable.ic_settings,
                onClick = {
                    navigate(MainActivityBloc.PagesConfig.SettingsCategoriesConfig())
                }
            )
            PreferenceRow(
                title = stringResource(R.string.backup_restore_title),
                drawableRes = R.drawable.ic_file_backup,
                onClick = {
                    navigate(BackupConfig)
                }
            )
            PreferenceRow(
                title = stringResource(R.string.title_folders),
                drawableRes = R.drawable.ic_folder,
                onClick = {
                    navigate(FoldersConfig)
                }
            )
            PreferenceRow(
                title = stringResource(R.string.learn_screen_title),
                drawableRes = R.drawable.ic_help,
                onClick = {
                    navigate(LearnConfig)
                }
            )
            PreferenceRow(
                title = stringResource(R.string.about_title),
                drawableRes = R.drawable.ic_info,
                onClick = {
                    navigate(AboutConfig)
                }
            )

            if (UpdateSystem.canUpdate) {
                AnimatedVisibility(autoUpdateChannel != UpdateChannel.Disabled) {
                    var latestRelease by remember { mutableStateOf<ReleaseBrief?>(null) }
                    LaunchedEffect(Unit) {
                        if (latestRelease == null) {
                            withContext(Dispatchers.IO) {
                                runCatching {
                                    latestRelease = UpdateSystem.getAvailableUpdateRelease(
                                        currentVersion = BuildConfig.VERSION_NAME,
                                        allowBetas = autoUpdateChannel == UpdateChannel.Beta
                                    )
                                }
                            }
                        }
                    }
                    latestRelease?.let { release ->
                        AnimatedVisibility(
                            visible = release.name != updateDismissedName,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            UpdateFoundBox(
                                versionToUpdate = release.name,
                                onClick = { navigate(AutoUpdateConfig) },
                                onDismissed = {
                                    bloc.dismissUpdate(release)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateFoundBox(
    versionToUpdate: String,
    onClick: () -> Unit,
    onDismissed: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = with(MaterialTheme.colorScheme) {
        primaryContainer
    },
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
            .background(color = containerColor)
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 20000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )
        val shape = remember {
            RoundedPolygonShape(
                RoundedPolygon.star(
                    numVerticesPerRadius = 10,
                    innerRadius = 0.8f,
                    rounding = CornerRounding(0.3f)
                )
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(50.dp)
                .offset((-10).dp, 10.dp)
                .rotate(rotation)
                .border(
                    width = 3.dp,
                    color = with(MaterialTheme.colorScheme) {
                        primary
                            .blend(onPrimaryContainer)
                            .copy(alpha = 0.5f)
                            .compositeOver(surface)
                    },
                    shape = shape
                )
        )
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = stringResource(R.string.update_found_version, versionToUpdate),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismissed) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.new_update_found_action),
                    style = MaterialTheme.typography.titleSmall,
                    color = LocalContentColor.current
                        .copy(alpha = 0.75f)
                )
            }
        }
    }
}