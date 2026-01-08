import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.jetbrains.kotlin.stdlib)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.jetbrains.kotlin.test)
            }
        }

        jvmMain {
            dependencies {}
        }

        jvmTest {
            dependencies {}
        }

    }
}