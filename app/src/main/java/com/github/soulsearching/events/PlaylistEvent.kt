package com.github.soulsearching.events

import android.graphics.Bitmap
import com.github.soulsearching.database.model.PlaylistWithMusics
import java.util.*

interface PlaylistEvent {
    object UpdatePlaylist : PlaylistEvent
    object DeletePlaylist : PlaylistEvent
    object UpdateQuickAccessState: PlaylistEvent
    data class SetSortDirection(val type: Int) : PlaylistEvent
    data class SetSortType(val type: Int) : PlaylistEvent
    data class AddPlaylist(val name : String) : PlaylistEvent
    data class AddFavoritePlaylist(val name : String) : PlaylistEvent
    data class SetSelectedPlaylist(val playlistWithMusics: PlaylistWithMusics) : PlaylistEvent
    data class TogglePlaylistSelectedState(val playlistId: UUID) : PlaylistEvent
    data class PlaylistFromId(val playlistId: UUID) : PlaylistEvent
    data class SetCover(val cover: Bitmap) : PlaylistEvent
    data class SetName(val name: String) : PlaylistEvent
    data class PlaylistsSelection(val musicId: UUID) : PlaylistEvent
    data class AddMusicToPlaylists(val musicId: UUID) : PlaylistEvent
    data class RemoveMusicFromPlaylist(val musicId: UUID) : PlaylistEvent
    data class BottomSheet(val isShown: Boolean) : PlaylistEvent
    data class DeleteDialog(val isShown: Boolean) : PlaylistEvent
    data class CreatePlaylistDialog(val isShown: Boolean) : PlaylistEvent
    data class AddNbPlayed(val playlistId: UUID): PlaylistEvent

}