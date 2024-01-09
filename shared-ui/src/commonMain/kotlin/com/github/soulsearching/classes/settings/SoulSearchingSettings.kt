package com.github.soulsearching.classes.settings

import com.github.soulsearching.classes.types.PlayerMode
import com.github.soulsearching.events.AlbumEvent
import com.github.soulsearching.events.ArtistEvent
import com.github.soulsearching.events.MusicEvent
import com.github.soulsearching.events.PlaylistEvent

/**
 * Represent the settings of a SoulSearching application where we can save key-value elements.
 */
interface SoulSearchingSettings {
    /**
     * Save an Integer related to a given key.
     */
    fun setInt(key: String, value: Int)

    /**
     * Save a String related to a given key.
     */
    fun setString(key: String, value: String)

    /**
     * Save a Boolean related to a given key.
     */
    fun setBoolean(key: String, value: Boolean)

    /**
     * Save a PlayerMode related to a given key.
     */
    fun setPlayerMode(key: String, value: PlayerMode)

    /**
     * Tries to retrieve an Integer related to a given key.
     */
    fun getInt(key: String, defaultValue: Int): Int

    /**
     * Tries to retrieve a Boolean related to a given key.
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    /**
     * Tries to retrieve a String related to a given key.
     */
    fun getString(key: String, defaultValue: String): String

    /**
     * Tries to retrieve a PlayerMode related to a given key.
     */
    fun getPlayerMode(): PlayerMode

    /**
     * Set the current played music index and position to the settings.
     */
    fun saveCurrentMusicInformation(
        currentMusicIndex: Int,
        currentMusicPosition: Int
    )

    /**
     * Initialize the sorts used by the application.
     */
    fun initializeSorts(
        onMusicEvent: (MusicEvent) -> Unit,
        onPlaylistEvent: (PlaylistEvent) -> Unit,
        onAlbumEvent: (AlbumEvent) -> Unit,
        onArtistEvent: (ArtistEvent) -> Unit
    )

    companion object {
        const val SHARED_PREF_KEY = "SOUL_SEARCHING_SHARED_PREF"

        const val HAS_MUSICS_BEEN_FETCHED_KEY = "MUSICS_FETCHED"

        const val SORT_MUSICS_TYPE_KEY = "SORT_MUSICS_TYPE"
        const val SORT_MUSICS_DIRECTION_KEY = "SORT_MUSICS_DIRECTION"

        const val SORT_ALBUMS_TYPE_KEY = "SORT_ALBUMS_TYPE"
        const val SORT_ALBUMS_DIRECTION_KEY = "SORT_ALBUMS_DIRECTION"

        const val SORT_ARTISTS_TYPE_KEY = "SORT_ARTISTS_TYPE"
        const val SORT_ARTISTS_DIRECTION_KEY = "SORT_ARTISTS_DIRECTION"

        const val SORT_PLAYLISTS_TYPE_KEY = "SORT_PLAYLISTS_TYPE"
        const val SORT_PLAYLISTS_DIRECTION_KEY = "SORT_PLAYLISTS_DIRECTION"

        const val PLAYER_MUSIC_INDEX_KEY = "PLAYER_MUSIC_INDEX"
        const val PLAYER_MUSIC_POSITION_KEY = "PLAYER_MUSIC_POSITION"
        const val PLAYER_MODE_KEY = "PLAYER_MODE_KEY"

        const val COLOR_THEME_KEY = "COLOR_THEME"
        const val DYNAMIC_PLAYER_THEME = "DYNAMIC_PLAYER_THEME"
        const val DYNAMIC_PLAYLIST_THEME = "DYNAMIC_PLAYLIST_THEME"
        const val DYNAMIC_OTHER_VIEWS_THEME = "DYNAMIC_OTHER_VIEWS_THEME"

        const val IS_QUICK_ACCESS_SHOWN = "IS_QUICK_ACCESS_SHOWN"
        const val IS_PLAYLISTS_SHOWN = "IS_PLAYLISTS_SHOWN"
        const val IS_ALBUMS_SHOWN = "IS_ALBUMS_SHOWN"
        const val IS_ARTISTS_SHOWN = "IS_ARTISTS_SHOWN"

        const val IS_VERTICAL_BAR_SHOWN = "IS_VERTICAL_BAR_SHOWN"
    }
}