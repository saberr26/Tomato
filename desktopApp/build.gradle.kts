/*
 * Copyright (c) 2026 Nishant Mishra
 *
 * This file is part of Tomato - a minimalist pomodoro timer for Android.
 *
 * Tomato is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tomato is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tomato.
 * If not, see <https://www.gnu.org/licenses/>.
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

/*
 * Copyright (c) 2026 Nishant Mishra
 *
 * This file is part of Tomato - a minimalist pomodoro timer for Android.
 *
 * Tomato is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tomato is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tomato.
 * If not, see <https://www.gnu.org/licenses/>.
 */
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":shared"))
    implementation(compose.desktop.currentOs)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.filekit.core)
}

compose.desktop {
    application {
        mainClass = "org.nsh07.pomodoro.MainKt"

        nativeDistributions {
            packageName = "Tomato"
            packageVersion = libs.versions.app.versionName.get()
            description = "Minimalist, data-oriented pomodoro timer"
            copyright = "Copyright (c) 2025-2026 Nishant Mishra"
            vendor = "Nishant Mishra"
            licenseFile.set(project.file("../LICENSE"))

            targetFormats(
                TargetFormat.AppImage,
                TargetFormat.Deb,
                TargetFormat.Rpm,
                TargetFormat.Dmg,
                TargetFormat.Exe
            )

            linux {
                iconFile = project.file("../shared/src/jvmMain/composeResources/drawable/logo.png")
                appRelease = libs.versions.app.versionCode.get()
                appCategory = "TIMER"

                val ogVersionName = libs.versions.app.versionName.get()

                debMaintainer = "nishant.28@outlook.com"
                debPackageVersion = ogVersionName.replace('-', '~')

                rpmLicenseType = "GPLv3"
                rpmPackageVersion = getNativePackageVersion(ogVersionName)
            }
            macOS {
                iconFile = project.file("src/main/logo.icns")
                bundleID = "org.nsh07.pomodoro"
                appCategory = "public.app-category.productivity"
                packageVersion = getNativePackageVersion(libs.versions.app.versionName.get())
            }
            windows {
                iconFile = project.file("src/main/logo.ico")
                description = "Tomato"
                console = false
                dirChooser = true
                perUserInstall = true
                shortcut = true
                packageVersion = getNativePackageVersion(libs.versions.app.versionName.get())
            }
        }

        buildTypes.release.proguard {
            isEnabled = false
            optimize = true
        }
    }
}

/**
 * Converts a SemVer string to a native-packager-friendly integer sequence.
 *
 * Example:
 *
 * ```
 * "1.8.6-alpha01" -> "1.8.51"
 * "1.8.6-beta02"  -> "1.8.552"
 * "1.8.6"         -> "1.8.60"
 * ```
 */
fun getNativePackageVersion(semanticVersion: String): String {
    val regex = """^(\d+)\.(\d+)\.(\d+)(?:-([a-zA-Z]+)(\d+))?$""".toRegex()

    val match = regex.matchEntire(semanticVersion)
        ?: throw IllegalArgumentException("Version string does not match the expected format: $semanticVersion")

    val (major, minor, patchStr, suffixType, suffixNumStr) = match.destructured
    val patch = patchStr.toInt()

    if (suffixType.isEmpty()) {
        // Appending '0' ensures the final release is mathematically higher
        // than any double-digit alpha/beta shifts (e.g., 60 > 51)
        return "$major.$minor.${patch}0"
    }

    require(patch > 0) {
        "Cannot shift patch version downwards for a pre-release because it is already 0 (e.g., 1.0.0-alpha01)."
    }

    val shiftedPatch = patch - 1
    var buildNumber = suffixNumStr.toInt() // Automatically drops leading zeros (e.g., "01" -> 1)

    // Optional: Offset beta versions so they upgrade correctly over alphas
    // Alphas stay in the 1-49 range, Betas jump into the 50+ range.
    if (suffixType.equals("beta", ignoreCase = true)) {
        buildNumber += 50
    }

    return "$major.$minor.$shiftedPatch$buildNumber"
}