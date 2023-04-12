package com.github.soulsearching.composables.bottomSheets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.soulsearching.Constants
import com.github.soulsearching.R
import com.github.soulsearching.events.MusicEvent

@Composable
fun MusicFileBottomSheet(
    modifyAction : () -> Unit,
    removeAction : () -> Unit
) {
    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.primary)
        .padding(Constants.Spacing.veryLarge),
        verticalArrangement = Arrangement.spacedBy(Constants.Spacing.veryLarge)
    ) {
        BottomSheetRow(icon = Icons.Default.PlaylistAdd, text = stringResource(id = R.string.add_to_playlist), onClick = {})
        BottomSheetRow(icon = Icons.Default.DoubleArrow, text = stringResource(id = R.string.add_to_shortcuts), onClick = {})
        BottomSheetRow(icon = Icons.Default.Delete, text = stringResource(id = R.string.delete_music), onClick = removeAction)
        BottomSheetRow(icon = Icons.Default.Edit, text = stringResource(id = R.string.modify_music), onClick = modifyAction)
        BottomSheetRow(icon = Icons.Default.PlaylistPlay, text = stringResource(id = R.string.play_next), onClick = {})
    }
}