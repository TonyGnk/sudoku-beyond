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

package gr.tonygnk.sudokubeyond.updates

import gr.tonygnk.sudokubeyond.updates.model.Version
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VersionTests {
    @Test
    fun testVersionStableComparison() {
        val version1 = Version.Stable(2, 0, 0)
        val version2 = Version.Stable(2, 1, 0)

        assertTrue(version1 < version2)
    }

    @Test
    fun testVersionBetaComparison() {
        val version1 = Version.Beta(2, 0, 0, 1)
        val version2 = Version.Beta(2, 0, 0, 2)

        assertTrue(version1 < version2)
    }

    @Test
    fun testVersionStableBetaComparison() {
        val version1 = Version.Stable(2, 0, 0)
        val version2 = Version.Beta(2, 0, 0, 1)

        assertTrue(version1 > version2)
    }

    @Test
    fun testMajorComparison() {
        val version1 = Version.Stable(2, 0, 0)
        val version2 = Version.Stable(3, 0, 0)

        assertTrue(version1 < version2)
    }

    @Test
    fun testMinorComparison() {
        val version1 = Version.Stable(2, 0, 0)
        val version2 = Version.Stable(2, 1, 0)

        assertTrue(version1 < version2)
    }

    @Test
    fun testPatchComparison() {
        val version1 = Version.Stable(2, 0, 0)
        val version2 = Version.Stable(2, 0, 1)

        assertTrue(version1 < version2)
    }

    @Test
    fun testBuildComparison() {
        val version1 = Version.Beta(2, 0, 0, 1)
        val version2 = Version.Beta(2, 0, 0, 2)

        assertTrue(version1 < version2)
    }

    @Test
    fun testVersionToString() {
        val version = Version.Stable(2, 0, 0)

        assertEquals(version.toVersionString(), "2.0.0")
    }

    @Test
    fun testVersionToStringBeta() {
        val version = Version.Beta(2, 0, 0, 1)

        assertEquals(version.toVersionString(), "2.0.0-beta1")
    }
}