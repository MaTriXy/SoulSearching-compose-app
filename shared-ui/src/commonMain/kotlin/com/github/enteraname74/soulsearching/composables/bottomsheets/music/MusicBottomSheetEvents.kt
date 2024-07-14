package com.github.enteraname74.soulsearching.composables.bottomsheets.music

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.github.enteraname74.domain.model.Music
import com.github.enteraname74.domain.model.PlaylistWithMusics
import com.github.enteraname74.soulsearching.coreui.dialog.SoulDialog
import com.github.enteraname74.soulsearching.coreui.navigation.SoulBackHandler
import com.github.enteraname74.soulsearching.coreui.strings.strings
import com.github.enteraname74.soulsearching.coreui.theme.color.SoulSearchingColorTheme
import com.github.enteraname74.soulsearching.domain.di.injectElement
import com.github.enteraname74.soulsearching.domain.model.types.MusicBottomSheetState
import com.github.enteraname74.soulsearching.feature.player.domain.model.PlaybackManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MusicBottomSheetEvents(
    selectedMusic: Music,
    currentPlaylistId: UUID? = null,
    playlistsWithMusics: List<PlaylistWithMusics>,
    navigateToModifyMusic: (String) -> Unit,
    musicBottomSheetState: MusicBottomSheetState = MusicBottomSheetState.NORMAL,
    isDeleteMusicDialogShown: Boolean,
    isBottomSheetShown: Boolean,
    isAddToPlaylistBottomSheetShown: Boolean,
    isRemoveFromPlaylistDialogShown: Boolean = false,
    onDismiss: () -> Unit,
    onSetDeleteMusicDialogVisibility: (Boolean) -> Unit,
    onSetRemoveMusicFromPlaylistDialogVisibility: (Boolean) -> Unit = {},
    onSetAddToPlaylistBottomSheetVisibility: (Boolean) -> Unit = {},
    onDeleteMusic: () -> Unit,
    onToggleQuickAccessState: () -> Unit,
    onRemoveFromPlaylist: () -> Unit = {},
    onAddMusicToSelectedPlaylists: (selectedPlaylistsIds: List<UUID>) -> Unit,
    primaryColor: Color = SoulSearchingColorTheme.colorScheme.primary,
    secondaryColor: Color = SoulSearchingColorTheme.colorScheme.secondary,
    onPrimaryColor: Color = SoulSearchingColorTheme.colorScheme.onPrimary,
    onSecondaryColor: Color = SoulSearchingColorTheme.colorScheme.onSecondary,
    playbackManager: PlaybackManager = injectElement(),
    retrieveCoverMethod: (UUID?) -> ImageBitmap?
) {
    val coroutineScope = rememberCoroutineScope()

    val musicModalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val addToPlaylistModalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    SoulBackHandler(addToPlaylistModalSheetState.isVisible) {
        coroutineScope.launch { addToPlaylistModalSheetState.hide() }
    }

    SoulBackHandler(musicModalSheetState.isVisible) {
        coroutineScope.launch { musicModalSheetState.hide() }
    }

    if (isDeleteMusicDialogShown) {
        SoulDialog(
            title = strings.deleteMusicDialogTitle,
            text = strings.deleteMusicDialogText,
            confirmAction = {
                onDeleteMusic()
                onSetDeleteMusicDialogVisibility(false)
                CoroutineScope(Dispatchers.IO).launch {
                    playbackManager.removeSongFromPlayedPlaylist(
                        musicId = selectedMusic.musicId
                    )
                }
                coroutineScope.launch { musicModalSheetState.hide() }
                    .invokeOnCompletion {
                        if (!musicModalSheetState.isVisible) onDismiss()
                    }
            },
            dismissAction = { onSetDeleteMusicDialogVisibility(false) },
            confirmText = strings.delete,
            dismissText = strings.cancel,
            backgroundColor = primaryColor,
            contentColor = onPrimaryColor
        )
    }

    if (isRemoveFromPlaylistDialogShown) {
        SoulDialog(
            title = strings.removeMusicFromPlaylistTitle,
            text = strings.removeMusicFromPlaylistText,
            confirmAction = {
                onRemoveFromPlaylist()
                onSetRemoveMusicFromPlaylistDialogVisibility(false)
                CoroutineScope(Dispatchers.IO).launch {
                    playbackManager.removeMusicIfSamePlaylist(
                        musicId = selectedMusic.musicId,
                        playlistId = currentPlaylistId
                    )
                }

                coroutineScope.launch { musicModalSheetState.hide() }
                    .invokeOnCompletion {
                        if (!musicModalSheetState.isVisible) onDismiss()
                    }
            },
            dismissAction = {
                onSetRemoveMusicFromPlaylistDialogVisibility(false)
            },
            confirmText = strings.delete,
            dismissText = strings.cancel,
            backgroundColor = primaryColor,
            contentColor = onPrimaryColor
        )
    }

    if (isBottomSheetShown) {
        MusicBottomSheet(
            musicModalSheetState = musicModalSheetState,
            selectedMusic = selectedMusic,
            onDismiss = onDismiss,
            onShowDeleteMusicDialog = { onSetDeleteMusicDialogVisibility(true) },
            onShowRemoveFromPlaylistDialog = { onSetRemoveMusicFromPlaylistDialogVisibility(true) },
            onToggleQuickAccessState = onToggleQuickAccessState,
            showAddToPlaylistBottomSheet = { onSetAddToPlaylistBottomSheetVisibility(true) },
            navigateToModifyMusic = navigateToModifyMusic,
            musicBottomSheetState = musicBottomSheetState,
            primaryColor = secondaryColor,
            textColor = onSecondaryColor,
            playbackManager = playbackManager
        )
    }

    if (isAddToPlaylistBottomSheetShown) {
        AddToPlaylistBottomSheet(
            addToPlaylistModalSheetState = addToPlaylistModalSheetState,
            primaryColor = secondaryColor,
            textColor = onSecondaryColor,
            playlistsWithMusics = playlistsWithMusics.filter { playlist ->
                playlist.musics.none { it.musicId == selectedMusic.musicId }
            },
            onDismiss = { onSetAddToPlaylistBottomSheetVisibility(false) },
            onConfirm = { selectedPlaylistsIds ->
                onAddMusicToSelectedPlaylists(selectedPlaylistsIds)
                onSetAddToPlaylistBottomSheetVisibility(false)
            },
            retrieveCoverMethod = retrieveCoverMethod
        )
    }
}