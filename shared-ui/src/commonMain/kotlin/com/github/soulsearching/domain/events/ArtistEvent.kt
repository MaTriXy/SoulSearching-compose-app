package com.github.soulsearching.domain.events

import androidx.compose.ui.graphics.ImageBitmap
import com.github.enteraname74.domain.model.ArtistWithMusics
import java.util.UUID

/**
 * Events related to artists.
 */
sealed interface ArtistEvent {

    data object DeleteArtist: ArtistEvent
    data object UpdateQuickAccessState: ArtistEvent
    data class SetSortDirection(val type: Int) : ArtistEvent
    data class SetSortType(val type: Int) : ArtistEvent
    data class SetSelectedArtistWithMusics(val artistWithMusics : ArtistWithMusics) : ArtistEvent

    data class BottomSheet(val isShown: Boolean) : ArtistEvent
    data class DeleteDialog(val isShown: Boolean) : ArtistEvent
    data class AddNbPlayed(val artistId: UUID): ArtistEvent

}