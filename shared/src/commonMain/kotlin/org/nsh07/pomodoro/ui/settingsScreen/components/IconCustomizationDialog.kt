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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.nsh07.pomodoro.ui.settingsScreen.viewModel.SettingsAction
import org.nsh07.pomodoro.ui.settingsScreen.viewModel.SettingsState
import org.nsh07.pomodoro.utils.toColor
import tomato.shared.generated.resources.Res
import tomato.shared.generated.resources.cancel
import tomato.shared.generated.resources.dynamic_color
import tomato.shared.generated.resources.ok
import tomato.shared.generated.resources.palette
import tomato.shared.generated.resources.restart

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IconCustomizationDialog(
    settingsState: SettingsState,
    onAction: (SettingsAction) -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Icon Customization", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(Res.string.dynamic_color))
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = settingsState.iconUseDynamic,
                    onCheckedChange = { onAction(SettingsAction.SaveIconUseDynamic(it)) }
                )
            }

            if (!settingsState.iconUseDynamic) {
                Spacer(Modifier.height(16.dp))
                Text("Hue")
                Slider(
                    value = 0f, // Simplified for now, would need conversion
                    onValueChange = { /* onAction(SettingsAction.SaveIconColor(Color(...))) */ }
                )
            }

            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { onAction(SettingsAction.ResetIconColors) }) {
                    Icon(painterResource(Res.drawable.restart), null)
                    Text("Reset")
                }
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.ok))
                }
            }
        }
    }
}
