package com.github.soulsearching.elementpage.artistpage.domain

import com.github.enteraname74.domain.model.ArtistWithMusics
import com.github.enteraname74.domain.model.PlaylistWithMusics

/**
 * State for managing a selected artist.
 */
data class SelectedArtistState(
    val artistWithMusics: ArtistWithMusics = ArtistWithMusics(),
    val allPlaylists: List<PlaylistWithMusics> = emptyList(),
    val isDeleteMusicDialogShown: Boolean = false,
    val isMusicBottomSheetShown: Boolean = false,
    val isAddToPlaylistBottomSheetShown: Boolean = false
)