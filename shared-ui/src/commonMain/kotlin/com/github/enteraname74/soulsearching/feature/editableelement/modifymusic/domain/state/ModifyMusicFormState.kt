package com.github.enteraname74.soulsearching.feature.editableelement.modifymusic.domain.state

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.github.enteraname74.domain.model.Music
import com.github.enteraname74.soulsearching.coreui.strings.strings
import com.github.enteraname74.soulsearching.coreui.textfield.SoulDropdownTextFieldHolderImpl
import com.github.enteraname74.soulsearching.coreui.textfield.SoulTextFieldHolder
import com.github.enteraname74.soulsearching.coreui.textfield.SoulTextFieldHolderImpl
import com.github.enteraname74.soulsearching.coreui.textfield.SoulTextFieldStyle

sealed interface ModifyMusicFormState {
    data object NoData : ModifyMusicFormState
    data class Data(
        private val initialMusic: Music,
        private val updateFoundAlbums: suspend (name: String) -> List<String>,
        private val updateFoundArtists: suspend (name: String) -> List<String>,
    ) : ModifyMusicFormState {
        val textFields: List<SoulTextFieldHolder> = listOf(
            SoulTextFieldHolderImpl(
                modifier = Modifier
                    .fillMaxWidth(),
                id = MUSIC_NAME,
                initialValue = initialMusic.name,
                isValid = { it.isNotBlank() },
                getLabel = { strings.musicName },
                style = SoulTextFieldStyle.Top,
                getError = { strings.fieldCannotBeEmpty },
            ),
            SoulDropdownTextFieldHolderImpl(
                modifier = Modifier
                    .fillMaxWidth(),
                id = ALBUM_NAME,
                isValid = { it.isNotBlank() },
                initialValue = initialMusic.album,
                updateProposedValues = updateFoundAlbums,
                getLabel = { strings.albumName },
                style = SoulTextFieldStyle.Body,
                getError = { strings.fieldCannotBeEmpty },
            ),
            SoulDropdownTextFieldHolderImpl(
                modifier = Modifier
                    .fillMaxWidth(),
                id = ARTIST_NAME,
                isValid = { it.isNotBlank() },
                initialValue = initialMusic.artist,
                updateProposedValues = updateFoundArtists,
                getLabel = { strings.artistName },
                style = SoulTextFieldStyle.Bottom,
                getError = { strings.fieldCannotBeEmpty },
            ),
        )

        fun isFormValid(): Boolean = textFields.all { it.isValid() }

        fun getMusicName(): String = textFields[0].value
        fun getAlbumName(): String = textFields[1].value
        fun getArtistName(): String = textFields[2].value

        companion object {
            private const val MUSIC_NAME = "MUSIC_NAME"
            private const val ALBUM_NAME = "ALBUM_NAME"
            private const val ARTIST_NAME = "ARTIST_NAME"
        }
    }
}