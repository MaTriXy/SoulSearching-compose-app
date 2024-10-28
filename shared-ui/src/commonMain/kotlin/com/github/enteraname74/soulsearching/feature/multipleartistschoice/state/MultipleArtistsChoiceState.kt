package com.github.enteraname74.soulsearching.feature.multipleartistschoice.state

import com.github.enteraname74.domain.model.Artist

sealed interface MultipleArtistChoiceState {
    data object Loading : MultipleArtistChoiceState
    data class UserAction(
        val artists: List<ArtistChoice>
    ): MultipleArtistChoiceState
}

data class ArtistChoice(
    val artist: Artist,
    val isSelected: Boolean = true,
)