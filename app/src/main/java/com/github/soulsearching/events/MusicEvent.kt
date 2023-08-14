package com.github.soulsearching.events

import android.graphics.Bitmap
import com.github.soulsearching.database.model.Music
import com.github.soulsearching.database.model.MusicPlaylist
import java.util.UUID

sealed interface MusicEvent {
    object AddMusic : MusicEvent
    object UpdateMusic : MusicEvent
    object DeleteMusic : MusicEvent
    object UpdateQuickAccessState: MusicEvent
    data class SetPlayedMusic(val music: Music) : MusicEvent
    data class SetSortDirection(val type: Int) : MusicEvent
    data class SetSortType(val type: Int) : MusicEvent
    data class DeleteDialog(val isShown: Boolean) : MusicEvent
    data class RemoveFromPlaylistDialog(val isShown: Boolean) : MusicEvent
    data class BottomSheet(val isShown: Boolean) : MusicEvent
    data class AddToPlaylistBottomSheet(val isShown: Boolean) : MusicEvent
    data class SetSelectedMusic(val music: Music) : MusicEvent
    data class SetName(val name: String) : MusicEvent
    data class SetArtist(val artist: String) : MusicEvent
    data class SetAlbum(val album: String) : MusicEvent
    data class SetCover(val cover: Bitmap) : MusicEvent
    data class DeleteMusicFromPlaylist(val musicPlaylist: MusicPlaylist) : MusicEvent

    data class SetFavorite(val musicId: UUID) : MusicEvent

}