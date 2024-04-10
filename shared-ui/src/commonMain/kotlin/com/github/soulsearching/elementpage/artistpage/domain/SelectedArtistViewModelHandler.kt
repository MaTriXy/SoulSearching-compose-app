package com.github.soulsearching.elementpage.artistpage.domain

import com.github.enteraname74.domain.model.ArtistWithMusics
import com.github.enteraname74.domain.model.MusicPlaylist
import com.github.enteraname74.domain.repository.ArtistRepository
import com.github.enteraname74.domain.repository.MusicPlaylistRepository
import com.github.enteraname74.domain.repository.MusicRepository
import com.github.enteraname74.domain.repository.PlaylistRepository
import com.github.soulsearching.domain.viewmodel.handler.ViewModelHandler
import com.github.soulsearching.player.domain.model.PlaybackManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Handler for managing the SelectedArtistViewModel.
 */
class SelectedArtistViewModelHandler(
    private val coroutineScope: CoroutineScope,
    private val artistRepository: ArtistRepository,
    private val playbackManager: PlaybackManager,
    private val musicRepository: MusicRepository,
    private val musicPlaylistRepository: MusicPlaylistRepository,
    playlistRepository: PlaylistRepository,
) : ViewModelHandler {
    private var _selectedArtistWithMusics: StateFlow<ArtistWithMusics?> = MutableStateFlow(
        ArtistWithMusics()
    )

    private val _playlists = playlistRepository.getAllPlaylistsWithMusicsSortByNameAscAsFlow()
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _state = MutableStateFlow(SelectedArtistState())
    var state = combine(
        _state,
        _playlists
    ) { state, playlists ->
        state.copy(
            allPlaylists = playlists
        )
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(5000),
        SelectedArtistState()
    )

    /**
     * Handles the events of the selected artist screen.
     */
    fun onEvent(event: SelectedArtistEvent) {
        when(event) {
            is SelectedArtistEvent.AddMusicToPlaylists -> addMusicToPlaylists(
                musicId = event.musicId,
                selectedPlaylistsIds = event.selectedPlaylistsIds
            )
            is SelectedArtistEvent.AddNbPlayed -> incrementNbPlayed(artistId = event.artistId)
            is SelectedArtistEvent.DeleteMusic -> deleteMusicFromApp(musicId = event.musicId)
            is SelectedArtistEvent.SetAddToPlaylistBottomSheetVisibility -> showOrHideAddToPlaylistBottomSheet(isShown = event.isShown)
            is SelectedArtistEvent.SetDeleteMusicDialogVisibility -> showOrHideDeleteDialog(isShown = event.isShown)
            is SelectedArtistEvent.SetMusicBottomSheetVisibility -> showOrHideMusicBottomSheet(isShown = event.isShown)
            is SelectedArtistEvent.SetSelectedArtist -> setSelectedArtist(artistId = event.artistId)
            is SelectedArtistEvent.ToggleQuickAccessState -> toggleQuickAccessState(musicId = event.musicId)
        }
    }

    /**
     * Set the selected artist.
     */
    private fun setSelectedArtist(artistId: UUID) {
        _selectedArtistWithMusics = artistRepository
            .getArtistWithMusicsAsFlow(artistId = artistId)
            .stateIn(
                coroutineScope, SharingStarted.WhileSubscribed(), ArtistWithMusics()
            )

        state =
            combine(
                _state,
                _selectedArtistWithMusics,
                _playlists
            ) { state, artist, playlists ->
                state.copy(
                    artistWithMusics = artist ?: ArtistWithMusics(),
                    allPlaylists = playlists
                )
            }.stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(5000),
                SelectedArtistState()
            )

        _state.update {
            it.copy(
                artistWithMusics = _selectedArtistWithMusics.value ?: ArtistWithMusics()
            )
        }
    }

    /**
     * Add a music to multiple playlists.
     */
    private fun addMusicToPlaylists(musicId: UUID, selectedPlaylistsIds: List<UUID>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (selectedPlaylistId in selectedPlaylistsIds) {
                musicPlaylistRepository.insertMusicIntoPlaylist(
                    MusicPlaylist(
                        musicId = musicId,
                        playlistId = selectedPlaylistId
                    )
                )
            }
        }
    }

    /**
     * Remove the selected music, from the MusicState, from the application
     */
    private fun deleteMusicFromApp(musicId: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            musicRepository.delete(musicId = musicId)
            playbackManager.removeSongFromLists(musicId = musicId)
        }
    }

    /**
     * Toggle the quick access state of the selected music.
     */
    private fun toggleQuickAccessState(musicId: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            musicRepository.toggleQuickAccessState(musicId = musicId)
        }
    }

    /**
     * Show or hide the delete dialog.
     */
    private fun showOrHideDeleteDialog(isShown: Boolean) {
        _state.update {
            it.copy(
                isDeleteMusicDialogShown = isShown
            )
        }
    }

    /**
     * Show or hide the add to playlist bottom sheet.
     */
    private fun showOrHideAddToPlaylistBottomSheet(isShown: Boolean) {
        _state.update {
            it.copy(
                isAddToPlaylistBottomSheetShown = isShown
            )
        }
    }

    /**
     * Show or hide the music bottom sheet.
     */
    private fun showOrHideMusicBottomSheet(isShown: Boolean) {
        _state.update {
            it.copy(
                isMusicBottomSheetShown = isShown
            )
        }
    }

    /**
     * Increment by one the number of time an artist was played.
     */
    private fun incrementNbPlayed(artistId: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            artistRepository.updateNbPlayed(
                newNbPlayed = artistRepository.getNbPlayedOfArtist(artistId) + 1,
                artistId = artistId
            )
        }
    }
}