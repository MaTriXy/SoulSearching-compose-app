package com.github.enteraname74.soulsearching.composables.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import com.github.enteraname74.soulsearching.coreui.UiConstants
import com.github.enteraname74.soulsearching.coreui.theme.color.SoulSearchingColorTheme

@Composable
fun NavigationRow(
    navigationRowSpec: NavigationRowSpec
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigationRowSpec.onClick()
            },
        horizontalArrangement = Arrangement.spacedBy(
            UiConstants.Spacing.large,
        )
    ) {
        Icon(
            modifier = Modifier
                .alpha(
                    if (navigationRowSpec.isSelected) {
                        1f
                    } else {
                        0f
                    }
                ),
            imageVector = navigationRowSpec.icon,
            tint = SoulSearchingColorTheme.colorScheme.onSecondary,
            contentDescription = null,
        )
        Text(
            text = navigationRowSpec.title,
            color = SoulSearchingColorTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (navigationRowSpec.isSelected) {
                FontWeight.ExtraBold
            } else {
                FontWeight.Normal
            }
        )
    }
}