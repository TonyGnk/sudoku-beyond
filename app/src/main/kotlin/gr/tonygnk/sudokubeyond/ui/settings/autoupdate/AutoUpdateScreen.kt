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

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.materialkolor.ktx.blend
import com.materialkolor.ktx.harmonize
import com.mikepenz.markdown.m3.Markdown
import gr.tonygnk.sudokubeyond.BuildConfig
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.update.DownloadStatus
import gr.tonygnk.sudokubeyond.core.update.Release
import gr.tonygnk.sudokubeyond.core.update.UpdateUtil
import gr.tonygnk.sudokubeyond.core.update.Version
import gr.tonygnk.sudokubeyond.core.update.toVersion
import gr.tonygnk.sudokubeyond.ui.components.collapsing_topappbar.CollapsingTitle
import gr.tonygnk.sudokubeyond.ui.components.collapsing_topappbar.CollapsingTopAppBar
import gr.tonygnk.sudokubeyond.ui.components.collapsing_topappbar.rememberTopAppBarScrollBehavior
import gr.tonygnk.sudokubeyond.ui.theme.RoundedPolygonShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoUpdateScreen(
    bloc: AutoUpdateBloc,
    finish: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = rememberTopAppBarScrollBehavior()

    val settings =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            UpdateUtil.installLatestApk(context)
        }

    val installApkLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                UpdateUtil.installLatestApk(context)
            } else {
                if (!context.packageManager.canRequestPackageInstalls())
                    settings.launch(
                        Intent(
                            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                            Uri.parse("package:${context.packageName}"),
                        )
                    )
                else UpdateUtil.installLatestApk(context)
            }
        }

    var latestRelease by remember { mutableStateOf<Release?>(null) }
    var currentDownloadStatus by remember { mutableStateOf(DownloadStatus.NotStarted as DownloadStatus) }
    val updateChannel by bloc.updateChannel.collectAsStateWithLifecycle(UpdateChannel.Disabled)
    var checkingForUpdates by rememberSaveable { mutableStateOf(false) }
    var checkingForUpdatesError by rememberSaveable { mutableStateOf(false) }
    var changeLogBottomSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(updateChannel) {
        if (updateChannel != UpdateChannel.Disabled) {
            checkingForUpdates = true
            latestRelease = null
            withContext(Dispatchers.IO) {
                runCatching {
                    latestRelease = UpdateUtil.checkForUpdate(updateChannel == UpdateChannel.Beta)
                        .also {
                            checkingForUpdates = false
                        }
                }
                    .onFailure {
                        Log.e("AutoUpdateScreen", "Failed to check for updates", it)
                        checkingForUpdates = false
                        checkingForUpdatesError = true
                    }
            }
        }

    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsingTopAppBar(
                collapsingTitle = CollapsingTitle.medium(titleText = stringResource(R.string.auto_update_title)),
                navigationIcon = {
                    IconButton(onClick = finish) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_small_left),
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnimatedVisibility(updateChannel != UpdateChannel.Disabled) {
                    AnimatedContent(latestRelease) { releaseState ->
                        if (releaseState != null && updateChannel != UpdateChannel.Disabled) {
                            NewUpdateContainer(
                                onDownloadClick = {
                                    if (latestRelease == null) return@NewUpdateContainer
                                    scope.launch(Dispatchers.IO) {
                                        runCatching {
                                            UpdateUtil.downloadApk(
                                                context = context,
                                                release = latestRelease!!
                                            ).collect { downloadStatus ->
                                                currentDownloadStatus = downloadStatus
                                                if (downloadStatus is DownloadStatus.Finished) {
                                                    installApkLauncher.launch(
                                                        Manifest.permission.REQUEST_INSTALL_PACKAGES
                                                    )
                                                }
                                            }
                                        }
                                            .onFailure {
                                                it.printStackTrace()
                                                currentDownloadStatus = DownloadStatus.NotStarted
                                                return@launch
                                            }
                                    }
                                },
                                onChangelogClick = { changeLogBottomSheet = true },
                                currentVersion = BuildConfig.VERSION_NAME.toVersion(),
                                newVersion = latestRelease?.name?.toVersion() ?: Version.Stable(
                                    0,
                                    0,
                                    0
                                ),
                                downloadStatus = currentDownloadStatus
                            )
                        } else if (updateChannel != UpdateChannel.Disabled) {
                            if (checkingForUpdatesError) {
                                CheckingUpdateContainer(
                                    text = stringResource(R.string.check_for_updates_error),
                                    subtitle = stringResource(R.string.check_for_update_error_summary),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            checkingForUpdatesError = false
                                            checkingForUpdates = true
                                            scope.launch {
                                                withContext(Dispatchers.IO) {
                                                    kotlin.runCatching {
                                                        latestRelease =
                                                            UpdateUtil.checkForUpdate(updateChannel == UpdateChannel.Beta)
                                                                .also {
                                                                    checkingForUpdates = false
                                                                }
                                                    }
                                                        .onFailure {
                                                            Log.e(
                                                                "AutoUpdateScreen",
                                                                "Failed to check for updates",
                                                                it
                                                            )
                                                            checkingForUpdates = false
                                                            checkingForUpdatesError = true
                                                        }
                                                }
                                            }
                                        },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text(text = stringResource(R.string.action_retry))
                                    }
                                }
                            } else {
                                CheckingUpdateContainer(
                                    text = if (checkingForUpdates) stringResource(R.string.check_for_updates_checking) else stringResource(
                                        R.string.check_for_update_using_latest
                                    ),
                                    subtitle = if (checkingForUpdates) stringResource(R.string.check_for_update_checking_summary) else stringResource(
                                        R.string.check_for_update_latest_summary
                                    ),
                                    drawableRes = if (checkingForUpdates) null else R.drawable.ic_verified,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateContentSize()
                                )
                            }
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.update_channel),
                        style = MaterialTheme.typography.titleMedium,
                        color = with(MaterialTheme.colorScheme) {
                            onSurface.harmonize(primary)
                        }
                    )
                    UpdateChannelItem(
                        title = stringResource(R.string.update_channel_stable),
                        subtitle = stringResource(R.string.update_channel_stable_summary),
                        drawableRes = R.drawable.ic_verified,
                        onClick = {
                            bloc.updateAutoUpdateChannel(UpdateChannel.Stable)
                        },
                        selected = updateChannel == UpdateChannel.Stable,
                    )
                    UpdateChannelItem(
                        title = stringResource(R.string.update_channel_beta),
                        subtitle = stringResource(R.string.update_channel_beta_summary),
                        drawableRes = R.drawable.ic_bug_report,
                        selectedColor = with(MaterialTheme.colorScheme) {
                            Color.Yellow.blend(
                                primary,
                                0.25f
                            ).copy(alpha = 0.45f)
                        },
                        onClick = {
                            bloc.updateAutoUpdateChannel(UpdateChannel.Beta)
                        },
                        selected = updateChannel == UpdateChannel.Beta
                    )
                    UpdateChannelItem(
                        title = stringResource(R.string.update_channel_disable),
                        subtitle = stringResource(R.string.update_channel_disable_summary),
                        drawableRes = R.drawable.ic_block,
                        selectedColor = with(MaterialTheme.colorScheme) {
                            Color.Red.blend(
                                primary,
                                0.25f
                            ).copy(alpha = 0.45f)
                        },
                        onClick = {
                            bloc.updateAutoUpdateChannel(UpdateChannel.Disabled)
                        },
                        selected = updateChannel == UpdateChannel.Disabled
                    )
                }
            }

            if (changeLogBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { changeLogBottomSheet = false }
                ) {
                    latestRelease?.let { release ->
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.whats_new_in_version,
                                    release.name.toString()
                                ),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Markdown(
                                    content = release.body.toString()
                                )
                                OutlinedButton(
                                    onClick = {
                                        context.startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(release.htmlUrl.toString())
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(vertical = 12.dp)
                                ) {
                                    Text(stringResource(R.string.action_view_on_github))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UpdateChannelItem(
    title: String,
    subtitle: String,
    @DrawableRes drawableRes: Int,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
    defaultColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    selectedColor: Color = with(MaterialTheme.colorScheme) {
        Color.Green.harmonize(primary).copy(alpha = 0.45f)
    },
) {
    val containerColor by animateColorAsState(
        if (selected) selectedColor else defaultColor,
    )
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(containerColor)
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(drawableRes),
            contentDescription = null
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleSmall,
                color = LocalContentColor.current.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
fun NewUpdateContainer(
    onDownloadClick: () -> Unit,
    onChangelogClick: () -> Unit,
    currentVersion: Version,
    newVersion: Version,
    downloadStatus: DownloadStatus,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val topStartShapeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    val topStartShape = remember {
        RoundedPolygonShape(
            RoundedPolygon.star(
                numVerticesPerRadius = 21,
                innerRadius = 0.8f,
                rounding = CornerRounding(0.3f)
            )
        )
    }
    val bottomEndShape = remember {
        RoundedPolygonShape(
            RoundedPolygon.star(
                numVerticesPerRadius = 21,
                innerRadius = 0.8f,
                rounding = CornerRounding(0.3f)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                color = with(MaterialTheme.colorScheme) { primaryContainer }
            )
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .offset((-50).dp, (-50).dp)
                .align(Alignment.TopStart)
                .rotate(topStartShapeRotation)
                .clip(topStartShape)
                .border(
                    BorderStroke(
                        width = 4.dp,
                        color = with(MaterialTheme.colorScheme) {
                            onPrimaryContainer
                                .blend(primary)
                                .copy(alpha = 0.5f)
                        }
                    ),
                    shape = topStartShape
                )
                .size(150.dp)
        )
        Box(
            modifier = Modifier
                .offset((50.dp), (40.dp))
                .align(Alignment.BottomEnd)
                .clip(bottomEndShape)
                .background(
                    color = with(MaterialTheme.colorScheme) {
                        onPrimaryContainer
                            .blend(primary)
                            .copy(alpha = 0.3f)
                    },
                    shape = topStartShape
                )
                .size(115.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_verified),
                contentDescription = null,
                tint = with(MaterialTheme.colorScheme) {
                    onPrimaryContainer.blend(
                        primary
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.update_found),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleMedium) {
                    Text(
                        text = currentVersion.toVersionString(),
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = newVersion.toVersionString(),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp)
            ) {
                TextButton(
                    onClick = onChangelogClick
                ) {
                    Text(stringResource(R.string.update_whats_new))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onDownloadClick,
                    enabled = downloadStatus !is DownloadStatus.Progress
                ) {
                    Text(
                        text = if (downloadStatus is DownloadStatus.Progress) {
                            downloadStatus.percent.toString()
                        } else {
                            stringResource(R.string.action_download)
                        }
                    )
                }
            }
        }
    }
}

enum class UpdateChannel {
    Disabled,
    Stable,
    Beta
}

@Composable
fun CheckingUpdateContainer(
    text: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    containerColor: Color = with(MaterialTheme.colorScheme) {
        primaryContainer
    },
    @DrawableRes drawableRes: Int? = null,
    action: @Composable ColumnScope.() -> Unit = { },
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
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
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
                AnimatedVisibility(
                    visible = drawableRes != null,
                ) {
                    if (drawableRes != null) {
                        Icon(
                            painter = painterResource(drawableRes),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                        )
                    }
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = LocalContentColor.current
                        .copy(alpha = 0.75f)
                )
                action()
            }
        }
    }
}