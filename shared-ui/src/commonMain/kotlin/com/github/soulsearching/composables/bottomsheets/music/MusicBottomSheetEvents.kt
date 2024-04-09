package com.github.soulsearching.composables.bottomsheets.music

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.github.enteraname74.domain.model.Music
import com.github.soulsearching.colortheme.domain.model.SoulSearchingColorTheme
import com.github.soulsearching.composables.SoulSearchingBackHandler
import com.github.soulsearching.composables.dialog.SoulSearchingDialog
import com.github.soulsearching.domain.di.injectElement
import com.github.soulsearching.domain.events.PlaylistEvent
import com.github.soulsearching.domain.model.types.BottomSheetStates
import com.github.soulsearching.domain.model.types.MusicBottomSheetState
import com.github.soulsearching.domain.viewmodel.PlayerMusicListViewModel
import com.github.soulsearching.player.domain.model.PlaybackManager
import com.github.soulsearching.strings.strings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
fun MusicBottomSheetEvents(
    selectedMusic: Music,
    navigateToModifyMusic: (String) -> Unit,
    musicBottomSheetState: MusicBottomSheetState = MusicBottomSheetState.NORMAL,
    playerMusicListViewModel: PlayerMusicListViewModel,
    playerDraggableState: SwipeableState<BottomSheetStates>,
    isDeleteMusicDialogShown: Boolean,
    isBottomSheetShown: Boolean,
    isAddToPlaylistBottomSheetShown: Boolean,
    onDismiss: () -> Unit,
    onSetDeleteMusicDialogVisibility: (Boolean) -> Unit,
    onSetRemoveMusicFromPlaylistVisibility: (Boolean) -> Unit,
    onDeleteMusic: () -> Unit,
    onToggleQuickAccessState: () -> Unit,
    onAddToPlaylist: () -> Unit,
    primaryColor: Color = SoulSearchingColorTheme.colorScheme.primary,
    secondaryColor: Color = SoulSearchingColorTheme.colorScheme.secondary,
    onPrimaryColor: Color = SoulSearchingColorTheme.colorScheme.onPrimary,
    onSecondaryColor: Color = SoulSearchingColorTheme.colorScheme.onSecondary,
    playbackManager: PlaybackManager = injectElement()
) {
    val coroutineScope = rememberCoroutineScope()

    val musicModalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val addToPlaylistModalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    SoulSearchingBackHandler(addToPlaylistModalSheetState.isVisible) {
        coroutineScope.launch { addToPlaylistModalSheetState.hide() }
    }

    SoulSearchingBackHandler(musicModalSheetState.isVisible) {
        coroutineScope.launch { musicModalSheetState.hide() }
    }

    if (isDeleteMusicDialogShown) {
        SoulSearchingDialog(
            title = strings.deleteMusicDialogTitle,
            text = strings.deleteMusicDialogText,
            confirmAction = {
                onDeleteMusic()
                onSetDeleteMusicDialogVisibility(false)
                CoroutineScope(Dispatchers.IO).launch {
                    playbackManager.removeSongFromPlayedPlaylist(
                        musicId = selectedMusic.musicId
                    )
                    playerMusicListViewModel.handler.savePlayerMusicList(
                        playbackManager.playedList.map { it.musicId }
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

    if (musicState.isRemoveFromPlaylistDialogShown) {
        SoulSearchingDialog(
            title = strings.removeMusicFromPlaylistTitle,
            text = strings.removeMusicFromPlaylistText,
            confirmAction = {
                onPlaylistsEvent(PlaylistEvent.RemoveMusicFromPlaylist(musicId = selectedMusic.musicId))
                onSetRemoveMusicFromPlaylistVisibility(false)
                CoroutineScope(Dispatchers.IO).launch {
                    playbackManager.removeMusicIfSamePlaylist(
                        musicId = musicState.selectedMusic.musicId,
                        playlistId = playlistState.selectedPlaylist.playlistId
                    )
                    playerMusicListViewModel.handler.savePlayerMusicList(
                        playbackManager.playedList.map { it.musicId }
                    )
                }

                coroutineScope.launch { musicModalSheetState.hide() }
                    .invokeOnCompletion {
                        if (!musicModalSheetState.isVisible) onDismiss()
                    }
            },
            dismissAction = {
                onSetRemoveMusicFromPlaylistVisibility(false)
            },
            confirmText = strings.delete,
            dismissText = strings.cancel,
            backgroundColor = primaryColor,
            contentColor = onPrimaryColor
        )
    }

    if (isBottomSheetShown) {
        MusicBottomSheet(
            musicBottomSheetState = musicBottomSheetState,
            musicModalSheetState = musicModalSheetState,
            navigateToModifyMusic = navigateToModifyMusic,
            playerMusicListViewModel = playerMusicListViewModel,
            playerDraggableState = playerDraggableState,
            primaryColor = secondaryColor,
            textColor = onSecondaryColor,
            selectedMusic = selectedMusic,
            onDismiss = onDismiss,
            onShowRemoveFromPlaylistDialog = { onSetRemoveMusicFromPlaylistVisibility(true) },
            onShowDeleteMusicDialog = { onSetDeleteMusicDialogVisibility(true) },
            onToggleQuickAccessState = onToggleQuickAccessState,
            onAddToPlaylist = onAddToPlaylist,
            playbackManager = playbackManager
        )
    }

    if (isAddToPlaylistBottomSheetShown) {
        AddToPlaylistBottomSheet(
            selectedMusicId = selectedMusic.musicId,
            onMusicEvent = onMusicEvent,
            onPlaylistsEvent = onPlaylistsEvent,
            addToPlaylistModalSheetState = addToPlaylistModalSheetState,
            playlistState = playlistState,
            primaryColor = secondaryColor,
            textColor = onSecondaryColor
        )
    }
}