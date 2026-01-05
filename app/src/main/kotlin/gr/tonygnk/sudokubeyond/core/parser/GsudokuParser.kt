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
import org.xmlpull.v1.XmlPullParserFactory

/**
 * .1gsudoku - file type from "Sudoku 10'000" app.
 */
class GsudokuParser : FileImportParser {
    private val tag = "GsudokuParser"

    /**
     * @param content .1gsudoku file content
     * @return Pair with: First - parsing success. Second - strings of parsed boards
     */
    override fun toBoards(content: String): Pair<Boolean, List<String>> {
        val parsedBoards = mutableListOf<String>()

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()

        val input = content.reader()
        parser.setInput(input)

        var eventType = parser.eventType
        try {
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.name == "sudoku") {
                    for (i in 0 until parser.attributeCount) {
                        if (parser.getAttributeName(i) == "data") {
                            val boardString = parser.getAttributeValue(i)
                            if (boardString.length == 81 && boardString.all { char -> char.isDigit() }) {
                                parsedBoards.add(boardString)
                            } else {
                                Log.i(tag, "Unexpected line: $boardString")
                                return Pair(false, parsedBoards)
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            Log.e(tag, "Exception while parsing!")
            e.printStackTrace()
            return Pair(false, parsedBoards)
        }

        input.close()

        return Pair(true, parsedBoards)
    }
}