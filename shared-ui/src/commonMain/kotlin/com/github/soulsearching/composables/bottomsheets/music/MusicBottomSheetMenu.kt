package com.github.soulsearching.composables.bottomsheets.music

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.automirrored.rounded.PlaylistPlay
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DoubleArrow
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.soulsearching.Constants
import com.github.soulsearching.composables.bottomsheets.BottomSheetRow
import com.github.soulsearching.strings
import com.github.soulsearching.theme.DynamicColor
import com.github.soulsearching.types.MusicBottomSheetState
import com.github.soulsearching.utils.SettingsUtils

@Composable
fun MusicBottomSheetMenu(
    modifyAction: () -> Unit,
    removeAction: () -> Unit,
    removeFromPlaylistAction: () -> Unit = {},
    removeFromPlayedListAction: () -> Unit = {},
    quickAccessAction: () -> Unit,
    addToPlaylistAction: () -> Unit,
    playNextAction : () -> Unit,
    musicBottomSheetState: MusicBottomSheetState = MusicBottomSheetState.NORMAL,
    isInQuickAccess: Boolean,
    primaryColor: Color = DynamicColor.secondary,
    textColor: Color = DynamicColor.onSecondary,
    isCurrentlyPlaying: Boolean
) {
    Column(
        modifier = Modifier
            .background(color = primaryColor)
            .padding(Constants.Spacing.large)
    ) {
        if (SettingsUtils.settingsViewModel.handler.isQuickAccessShown) {
            BottomSheetRow(
                icon = Icons.Rounded.DoubleArrow,
                text = if (isInQuickAccess) {
                    strings.removeFromQuickAccess
                } else {
                    strings.addToQuickAccess
                },
                onClick = quickAccessAction,
                textColor = textColor
            )
        }
        BottomSheetRow(
            icon = Icons.AutoMirrored.Rounded.PlaylistAdd,
            text = strings.addToPlaylist,
            onClick = addToPlaylistAction,
            textColor = textColor
        )
        BottomSheetRow(
            icon = Icons.Rounded.Edit,
            text = strings.modifyMusic,
            onClick = modifyAction,
            textColor = textColor
        )
        if (!isCurrentlyPlaying) {
            BottomSheetRow(
                icon = Icons.AutoMirrored.Rounded.PlaylistPlay,
                text = strings.playNext,
                onClick = playNextAction,
                textColor = textColor
            )
        }
        if (musicBottomSheetState == MusicBottomSheetState.PLAYLIST) {
            BottomSheetRow(
                icon = Icons.Rounded.Delete,
                text = strings.removeFromPlaylist,
                onClick = removeFromPlaylistAction,
                textColor = textColor
            )
        }
        if (musicBottomSheetState == MusicBottomSheetState.PLAYER) {
            BottomSheetRow(
                icon = Icons.Rounded.Delete,
                text = strings.removeFromPlayedList,
                onClick = removeFromPlayedListAction,
                textColor = textColor
            )
        }
        BottomSheetRow(
            icon = Icons.Rounded.Delete,
            text = strings.deleteMusic,
            onClick = removeAction,
            textColor = textColor
        )
    }
}