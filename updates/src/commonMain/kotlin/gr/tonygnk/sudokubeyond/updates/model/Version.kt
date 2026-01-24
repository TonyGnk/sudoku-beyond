/*
 *  Copyright (C) 2026 TonyGnk
 *
 *  This file is part of Sudoku Beyond.
 *  Originally from LibreSudoku (https://github.com/kaajjo/LibreSudoku)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */

package gr.tonygnk.sudokubeyond.updates.model

sealed class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val build: Int = 0,
) : Comparable<Version> {
    abstract fun toVersionString(): String
    abstract fun toVersionNumber(): Long

    override fun compareTo(other: Version): Int = this.toVersionNumber().compareTo(other.toVersionNumber())

    class Stable(major: Int, minor: Int, patch: Int) : Version(major, minor, patch) {
        override fun toVersionString(): String = "$major.$minor.$patch"

        override fun toVersionNumber(): Long = (major * MAJOR) + (minor * MINOR) + (patch * PATCH) + STABLE
    }

    class Beta(major: Int, minor: Int, patch: Int, build: Int) :
        Version(major, minor, patch, build) {
        override fun toVersionString(): String = "$major.$minor.$patch-beta$build"

        override fun toVersionNumber(): Long =
            (major * MAJOR) + (minor * MINOR) + (patch * PATCH) + (build * BUILD)
    }

    companion object {
        private const val MAJOR = 10_000_000L
        private const val MINOR = 100_000L
        private const val PATCH = 1000L
        private const val BUILD = 10L
        private const val STABLE = 100L
    }
}

fun String.toVersion(): Version {
    this.replace("v", "").run {
        val regex = Regex("""(\d+)\.(\d+)\.(\d+)(?:-(?:alpha|beta)?(\d+))?""")
        val matchResult = regex.matchEntire(this)

        val (major, minor, patch, build) = matchResult!!.destructured
        try {
            major.toInt()
            minor.toInt()
            patch.toInt()
        } catch (e: Exception) {
            println("Failed to parse version name: $this - ${e.message}")
        }
        return if (this.contains("beta")) {
            Version.Beta(major.toInt(), minor.toInt(), patch.toInt(), build.toInt())
        } else {
            Version.Stable(major.toInt(), minor.toInt(), patch.toInt())
        }
    }
}