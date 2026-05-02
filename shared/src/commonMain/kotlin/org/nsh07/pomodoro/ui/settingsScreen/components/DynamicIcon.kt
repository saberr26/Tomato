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

package org.nsh07.pomodoro.ui.settingsScreen.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.nsh07.pomodoro.ui.settingsScreen.viewModel.SettingsState
import org.nsh07.pomodoro.utils.toColor
import tomato.shared.generated.resources.Res
import tomato.shared.generated.resources.tomato_logo_notification

@Composable
fun DynamicIcon(
    settingsState: SettingsState,
    modifier: Modifier = Modifier
) {
    val tint = if (settingsState.iconUseDynamic) {
        colorScheme.primary
    } else {
        settingsState.iconColor.toColor()
    }

    Icon(
        painter = painterResource(Res.drawable.tomato_logo_notification),
        contentDescription = null,
        tint = tint,
        modifier = modifier
    )
}
