package com.github.enteraname74.soulsearching.coreui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.enteraname74.soulsearching.coreui.theme.color.SoulSearchingColorTheme

@Composable
fun SoulAlertDialog(
    confirmAction: () -> Unit,
    dismissAction: () -> Unit,
    title: String,
    text: String = "",
    icon: @Composable (() -> Unit)? = null,
    confirmText: String,
    dismissText: String? = null,
    backgroundColor: Color = SoulSearchingColorTheme.colorScheme.primary,
    contentColor: Color = SoulSearchingColorTheme.colorScheme.onPrimary
) {
    AlertDialog(
        onDismissRequest = dismissAction,
        confirmButton = {
            TextButton(onClick = { confirmAction() }) {
                Text(
                    text = confirmText,
                    color = contentColor
                )
            }
        },
        dismissButton = dismissText?.let {
            {
                TextButton(onClick = dismissAction) {
                    Text(
                        text = it,
                        color = contentColor
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = text,
                textAlign = TextAlign.Center
            )
        },
        icon = icon,
        containerColor = backgroundColor,
        textContentColor = contentColor,
        titleContentColor = contentColor,
        iconContentColor = contentColor
    )
}

@Composable
fun SoulAlertDialog(
    confirmAction: () -> Unit,
    dismissAction: () -> Unit,
    title: String,
    content: @Composable () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    confirmText: String,
    dismissText: String,
    backgroundColor: Color = SoulSearchingColorTheme.colorScheme.primary,
    contentColor: Color = SoulSearchingColorTheme.colorScheme.onPrimary
) {
    AlertDialog(
        onDismissRequest = dismissAction,
        confirmButton = {
            TextButton(onClick = { confirmAction() }) {
                Text(
                    text = confirmText,
                    color = contentColor
                )
            }
        },
        dismissButton = {
            TextButton(onClick = dismissAction) {
                Text(
                    text = dismissText,
                    color = contentColor
                )
            }
        },
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            content()
        },
        icon = icon,
        containerColor = backgroundColor,
        textContentColor = contentColor,
        titleContentColor = contentColor,
        iconContentColor = contentColor
    )
}