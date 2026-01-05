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
import java.time.Duration

/**
 * Converts Duration
 */
class DurationConverter {
    /**
     * Converts seconds to Duration
     * @param value Duration in seconds
     * @return duration of seconds
     */
    @TypeConverter
    fun toDuration(value: Long): Duration {
        return Duration.ofSeconds(value)
    }

    /**
     * Converts Duration to seconds
     * @param duration Duration
     * @return duration represented in seconds
     */
    @TypeConverter
    fun fromDuration(duration: Duration): Long {
        return duration.seconds
    }
}