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

package gr.tonygnk.sudokubeyond.core.parser

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException


// File type from OpenSudoku app (https://gitlab.com/opensudoku/opensudoku)
// https://gitlab.com/opensudoku/opensudoku/-/blob/develop/app/src/main/java/org/moire/opensudoku/gui/importing/OpenSudokuImportTask.java
/**
 * .opensudoku - format from the OpenSudoku app. Uses XML schema
 */
class OpenSudokuParser : FileImportParser {
    private val tag = "OpenSudokuParser"

    /**
     * @param content .opensudoku file content
     * @return Pair with: First - parsing success. Second - strings of parsed boards
     */
    override fun toBoards(content: String): Pair<Boolean, List<String>> {
        var result: Pair<Boolean, List<String>> = Pair(false, emptyList())

        val factory: XmlPullParserFactory
        val parser: XmlPullParser
        try {
            factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            parser = factory.newPullParser()
            parser.setInput(content.reader())
            var eventType = parser.eventType
            var rootTag: String
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    rootTag = parser.name
                    if (rootTag == "opensudoku") {
                        val version = parser.getAttributeValue(null, "version")
                        if (version == null) {
                            // no version provided, assume that it's version 1
                            result = importV1(parser)
                        } else if (version == "2") {
                            result = importV2(parser)
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
            return result.copy(first = false)
        } catch (e: IOException) {
            e.printStackTrace()
            return result.copy(first = false)
        }
        return result
    }

    private fun importV1(parser: XmlPullParser): Pair<Boolean, List<String>> {
        val boards = mutableListOf<String>()
        var eventType = parser.eventType
        var lastTag = ""
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                lastTag = parser.name
                if (lastTag == "game") {
                    val boardString = parser.getAttributeValue(null, "data")

                    if (boardString.length == 81 && boardString.all { char -> char.isDigit() }) {
                        boards.add(boardString)
                    } else {
                        Log.i("$tag/ImportV1", "This line was skipped $boardString")
                    }
                }
            }
            eventType = parser.next()
        }
        return Pair(true, boards)
    }


    private fun importV2(parser: XmlPullParser): Pair<Boolean, List<String>> {
        var eventType = parser.eventType
        var lastTag = ""
        //var folderName: String? = null
        val boards = mutableListOf<String>()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                lastTag = parser.name
                if (lastTag == "folder") {
                    // folderName = parser.getAttributeValue(null, "name")
                } else if (lastTag == "game") {
                    // Not used now, but maybe will be used in future
                    // val created = parseLong(parser.getAttributeValue(null, "created"), System.currentTimeMillis());
                    // val lastPlayed = parseLong(parser.getAttributeValue(null, "last_played"), 0);
                    // val note = parser.getAttributeValue(null, "note");
                    // val state = parseLong(parser.getAttributeValue(null, "state"))
                    // val timer = parseLong(parser.getAttributeValue(null, "time"), 0)

                    val boardString = parser.getAttributeValue(null, "data")
                    if (boardString.length == 81) {
                        boards.add(boardString)
                    } else {
                        Log.i("$tag/ImportV2", "This line was skipped $boardString")
                    }
                }
            }
            eventType = parser.next()
        }
        return Pair(true, boards)
    }
}