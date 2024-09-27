package com.github.enteraname74.soulsearching.composables.bottomsheets.artist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import com.github.enteraname74.soulsearching.composables.bottomsheets.BottomSheetRow
import com.github.enteraname74.soulsearching.composables.bottomsheets.QuickAccessBottomSheetMenu
import com.github.enteraname74.soulsearching.coreui.strings.strings

@Composable
fun ArtistBottomSheetMenu(
    modifyAction: () -> Unit,
    deleteAction: () -> Unit,
    quickAccessAction: () -> Unit,
    isInQuickAccess: Boolean,
) {
    QuickAccessBottomSheetMenu(
        isElementInQuickAccess = isInQuickAccess,
        quickAccessAction = quickAccessAction,
    ) {
        BottomSheetRow(
            icon = Icons.Rounded.Edit,
            text = strings.modifyArtist,
            onClick = modifyAction
        )
        BottomSheetRow(
            icon = Icons.Rounded.Delete,
            text = strings.deleteArtist,
            onClick = deleteAction
        )
    }
}
