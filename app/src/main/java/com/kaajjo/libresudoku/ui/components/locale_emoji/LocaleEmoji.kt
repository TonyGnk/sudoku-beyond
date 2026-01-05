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

package com.kaajjo.libresudoku.ui.components.locale_emoji

import android.util.Log

object LocaleEmoji {
    /**
     * Get a country flag from language code
     * @return country flag (emoji)
     */
    fun getFlagEmoji(
        languageCode: String
    ): String? {
        val countryCode = countryFromLanguage(languageCode)
        if (countryCode.isNullOrBlank()) return null

        return countryCodeToEmoji(countryCode)
    }

    private fun countryFromLanguage(languageCode: String?): String? {
        if (languageCode == null) return null

        return languageCodeToCountryCode[languageCode.lowercase()]
    }

    private fun countryCodeToEmoji(countryCode: String): String? {
        val uppercaseCode = countryCode.uppercase()

        try {
            val firstChar = uppercaseCode[0] - 'A' + 0x1F1E6
            val secondChar = uppercaseCode[1] - 'A' + 0x1F1E6
            val emoji = String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
            return emoji
        } catch (e: IllegalArgumentException) {
            Log.e("LocaleEmoji", "Cannot find flag for: $countryCode")
            return null
        }
    }
}