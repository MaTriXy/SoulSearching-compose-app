package com.github.soulsearching.composables.bottomSheets.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.soulsearching.Constants
import com.github.soulsearching.R
import com.github.soulsearching.composables.bottomSheets.BottomSheetRow

@Composable
fun ArtistBottomSheetMenu(
    modifyAction: () -> Unit,
    deleteAction: () -> Unit,
    addToShortcutsAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(Constants.Spacing.large)
    ) {
        BottomSheetRow(
            icon = Icons.Default.DoubleArrow,
            text = stringResource(id = R.string.add_to_shortcuts),
            onClick = addToShortcutsAction
        )
        BottomSheetRow(
            icon = Icons.Default.Edit,
            text = stringResource(id = R.string.modify_album),
            onClick = modifyAction
        )
        BottomSheetRow(
            icon = Icons.Default.Delete,
            text = stringResource(id = R.string.delete_album),
            onClick = deleteAction
        )
    }
}
