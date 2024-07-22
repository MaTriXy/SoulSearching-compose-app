package com.github.enteraname74.soulsearching.feature.mainpage.domain.state

import androidx.compose.ui.graphics.ImageBitmap
import com.github.enteraname74.domain.model.Playlist
import com.github.enteraname74.domain.model.PlaylistWithMusics
import com.github.enteraname74.domain.model.PlaylistWithMusicsNumber
import com.github.enteraname74.domain.model.SortDirection
import com.github.enteraname74.domain.model.SortType
import java.util.UUID

/**
 * Manage elements related to playlists.
 */
data class PlaylistState(
    val playlists: List<PlaylistWithMusicsNumber> = emptyList(),
    val selectedPlaylist: Playlist = Playlist(),
    val playlistsWithMusics: List<PlaylistWithMusics> = emptyList(),
    val multiplePlaylistSelected: ArrayList<UUID> = ArrayList(),
    val cover: ImageBitmap? = null,
    val name: String = "",
    val hasSetNewCover: Boolean = false,
    var sortType: Int = SortType.NAME,
    var sortDirection: Int = SortDirection.ASC
)