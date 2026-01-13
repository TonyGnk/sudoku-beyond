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

package gr.tonygnk.sudokubeyond.ui.more.about

import android.R.attr.contentDescription
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gr.tonygnk.sudokubeyond.BuildConfig
import gr.tonygnk.sudokubeyond.R
import gr.tonygnk.sudokubeyond.core.CARD_MIR
import gr.tonygnk.sudokubeyond.core.CRYPTO_BTC
import gr.tonygnk.sudokubeyond.core.CRYPTO_TON
import gr.tonygnk.sudokubeyond.core.CRYPTO_USDT_TRC20
import gr.tonygnk.sudokubeyond.core.GITHUB_REPOSITORY
import gr.tonygnk.sudokubeyond.core.TELEGRAM_CHANNEL
import gr.tonygnk.sudokubeyond.core.WEBLATE_ENGAGE
import gr.tonygnk.sudokubeyond.ui.app.bloc.MainActivityBloc
import gr.tonygnk.sudokubeyond.ui.theme.ColorUtils.harmonizeWithPrimary
import gr.tonygnk.sudokubeyond.util.FlavorUtil

data object AboutBloc : MainActivityBloc.PagesBloc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun AboutScreen(
    navigate: (MainActivityBloc.PagesConfig) -> Unit = {},
    finish: () -> Unit= {},
) {
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = finish) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_small_left),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .size(86.dp),
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    stringResource(
                        R.string.app_version,
                        BuildConfig.VERSION_NAME + if (FlavorUtil.isFoss()) "-FOSS" else "",
                        BuildConfig.VERSION_CODE
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }

            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 3
            ) {
                AboutSectionBox(
                    title = stringResource(R.string.about_github_project),
                    subtitle = stringResource(R.string.about_github_source_code),
                    drawableRes = R.drawable.ic_github,
                    onClick = { uriHandler.openUri(GITHUB_REPOSITORY) }
                )
                AboutSectionBox(
                    title = stringResource(R.string.weblate),
                    subtitle = stringResource(R.string.help_translate),
                    drawableRes = R.drawable.ic_weblate,
                    onClick = { uriHandler.openUri(WEBLATE_ENGAGE) }
                )
                AboutSectionBox(
                    title = stringResource(R.string.telegram),
                    subtitle = stringResource(R.string.telegram_link),
                    drawableRes = R.drawable.ic_telegram,
                    onClick = { uriHandler.openUri(TELEGRAM_CHANNEL) }
                )
                AboutSectionBox(
                    title = stringResource(R.string.libraries_licenses),
                    subtitle = stringResource(R.string.libraries_licenses_title),
                    drawableRes = R.drawable.ic_info,
                    onClick = {
                        navigate(MainActivityBloc.PagesConfig.AboutLibrariesConfig)
                    }
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_payments),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.donation_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(7.dp))
                    ) {
                        Text(
                            text = stringResource(R.string.donation_description),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DonationItem(
                            title = stringResource(R.string.crypto_bitcoin),
                            information = CRYPTO_BTC,
                            drawableRes = R.drawable.ic_coins_crypto,
                            onClick = { clipboardManager.setText(AnnotatedString(text = CRYPTO_BTC)) }
                        )
                        DonationItem(
                            title = stringResource(R.string.crypto_ton),
                            information = CRYPTO_TON,
                            drawableRes = R.drawable.ic_crypto_ton,
                            onClick = { clipboardManager.setText(AnnotatedString(text = CRYPTO_TON)) }
                        )
                        DonationItem(
                            title = stringResource(R.string.crypto_usdt),
                            information = CRYPTO_USDT_TRC20,
                            drawableRes = R.drawable.ic_usdt,
                            onClick = { clipboardManager.setText(AnnotatedString(text = CRYPTO_USDT_TRC20)) }
                        )
                        DonationItem(
                            title = stringResource(R.string.card_mir),
                            information = CARD_MIR,
                            drawableRes = R.drawable.ic_mir,
                            onClick = { clipboardManager.setText(AnnotatedString(text = CARD_MIR.filter { it != ' ' })) }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.AboutSectionBox(
    title: String,
    @DrawableRes drawableRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    additionalContent: @Composable (ColumnScope.() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxRowHeight()
            .weight(1f)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(drawableRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.harmonizeWithPrimary(),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = subtitle
                )
            }
            if (additionalContent != null) {
                additionalContent()
            }
        }
    }
}

@Composable
fun DonationItem(
    title: String,
    information: String,
    @DrawableRes drawableRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest.harmonizeWithPrimary())
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(drawableRes),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = information,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(R.drawable.ic_content_copy),
                contentDescription = null
            )
        }
    }
}