package com.github.soulsearching.elementpage.playlistpage.presentation.composable

import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.github.enteraname74.domain.model.Music
import com.github.enteraname74.domain.model.PlaylistWithMusics
import com.github.soulsearching.Constants
import com.github.soulsearching.colortheme.domain.model.SoulSearchingColorTheme
import com.github.soulsearching.composables.MusicItemComposable
import com.github.soulsearching.composables.PlayerSpacer
import com.github.soulsearching.composables.bottomsheets.music.MusicBottomSheetEvents
import com.github.soulsearching.domain.di.injectElement
import com.github.soulsearching.domain.model.types.BottomSheetStates
import com.github.soulsearching.domain.model.types.MusicBottomSheetState
import com.github.soulsearching.player.domain.model.PlaybackManager
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicList(
    modifier: Modifier = Modifier,
    selectedMusic: Music?,
    onSelectMusic: (Music) -> Unit,
    musics: List<Music>,
    playlistsWithMusics: List<PlaylistWithMusics>,
    playlistId: UUID?,
    isDeleteMusicDialogShown: Boolean,
    isBottomSheetShown: Boolean,
    isAddToPlaylistBottomSheetShown: Boolean,
    isRemoveFromPlaylistDialogShown: Boolean = false,
    onSetBottomSheetVisibility: (Boolean) -> Unit,
    onSetDeleteMusicDialogVisibility: (Boolean) -> Unit,
    onSetRemoveMusicFromPlaylistDialogVisibility: (Boolean) -> Unit = {},
    onSetAddToPlaylistBottomSheetVisibility: (Boolean) -> Unit = {},
    onDeleteMusic: (Music) -> Unit,
    onToggleQuickAccessState: (Music) -> Unit,
    onRemoveFromPlaylist: (Music) -> Unit = {},
    onAddMusicToSelectedPlaylists: (selectedPlaylistsIds: List<UUID>, selectedMusic: Music) -> Unit,
    navigateToModifyMusic: (String) -> Unit,
    retrieveCoverMethod: (UUID?) -> ImageBitmap?,
    isMainPlaylist: Boolean = false,
    updateNbPlayedAction: (UUID) -> Unit,
    musicBottomSheetState: MusicBottomSheetState = MusicBottomSheetState.NORMAL,
    playerDraggableState: SwipeableState<BottomSheetStates>,
    primaryColor: Color = SoulSearchingColorTheme.colorScheme.primary,
    secondaryColor: Color = SoulSearchingColorTheme.colorScheme.secondary,
    onPrimaryColor: Color = SoulSearchingColorTheme.colorScheme.onPrimary,
    onSecondaryColor: Color = SoulSearchingColorTheme.colorScheme.onSecondary,
    playbackManager: PlaybackManager = injectElement()
) {
    val coroutineScope = rememberCoroutineScope()

    selectedMusic?.let { music ->
        MusicBottomSheetEvents(
            selectedMusic = music,
            currentPlaylistId = playlistId,
            playlistsWithMusics = playlistsWithMusics,
            navigateToModifyMusic = navigateToModifyMusic,
            musicBottomSheetState = musicBottomSheetState,
            playerDraggableState = playerDraggableState,
            isDeleteMusicDialogShown = isDeleteMusicDialogShown,
            isBottomSheetShown = isBottomSheetShown,
            isAddToPlaylistBottomSheetShown = isAddToPlaylistBottomSheetShown,
            isRemoveFromPlaylistDialogShown = isRemoveFromPlaylistDialogShown,
            onDismiss = { onSetBottomSheetVisibility(false) },
            onSetDeleteMusicDialogVisibility = onSetDeleteMusicDialogVisibility,
            onSetRemoveMusicFromPlaylistDialogVisibility = onSetRemoveMusicFromPlaylistDialogVisibility,
            onSetAddToPlaylistBottomSheetVisibility = onSetAddToPlaylistBottomSheetVisibility,
            onDeleteMusic = { onDeleteMusic(music) },
            onToggleQuickAccessState = { onToggleQuickAccessState(music) },
            onRemoveFromPlaylist = { onRemoveFromPlaylist(music) },
            onAddMusicToSelectedPlaylists = { selectedPlaylistsIds ->
                onAddMusicToSelectedPlaylists(selectedPlaylistsIds, music)
            },
            primaryColor = primaryColor,
            secondaryColor = secondaryColor,
            onPrimaryColor = onPrimaryColor,
            onSecondaryColor = onSecondaryColor
        )
    }


    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = musics
        ) { music ->
            MusicItemComposable(
                music = music,
                onClick = {
                    coroutineScope.launch {
                        playerDraggableState.animateTo(BottomSheetStates.EXPANDED, tween(Constants.AnimationDuration.normal))
                    }.invokeOnCompletion {
                        playlistId?.let {
                            updateNbPlayedAction(it)
                        }

                        playbackManager.setCurrentPlaylistAndMusic(
                            music = music,
                            musicList = musics,
                            playlistId = playlistId,
                            isMainPlaylist = isMainPlaylist
                        )
                    }
                },
                onLongClick = {
                    coroutineScope.launch {
                        onSelectMusic(music)
                        onSetBottomSheetVisibility(true)
                    }
                },
                musicCover = retrieveCoverMethod(music.coverId),
                textColor = onPrimaryColor,
                isPlayedMusic = playbackManager.isSameMusicAsCurrentPlayedOne(music.musicId)
            )
        }
        item {
            PlayerSpacer()
        }
    }
}