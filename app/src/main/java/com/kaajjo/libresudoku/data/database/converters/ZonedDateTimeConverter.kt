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

package com.kaajjo.libresudoku.data.database.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Converts date represented by Instant to Long seconds and reverse
 */
class ZonedDateTimeConverter {
    /**
     * Converts date in seconds to ZoneDateTime with system default time zone
     * @param value date in seconds
     * @return ZonedDateTime of seconds
     */
    @TypeConverter
    fun toZonedDateTime(value: Long?): ZonedDateTime? {
        return if (value != null) {
            ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(value),
                ZoneId.systemDefault()
            )
        } else {
            null
        }

    }

    /**
     * Converts ZonedDateTime to seconds
     * @param zonedDateTime date
     */
    @TypeConverter
    fun fromZonedDateTime(zonedDateTime: ZonedDateTime?): Long? {
        return zonedDateTime?.toInstant()?.epochSecond
    }
}