package com.github.soulsearching.viewmodel.handler

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.github.enteraname74.domain.model.Music
import com.github.enteraname74.domain.repository.AlbumArtistRepository
import com.github.enteraname74.domain.repository.AlbumRepository
import com.github.enteraname74.domain.repository.ArtistRepository
import com.github.enteraname74.domain.repository.ImageCoverRepository
import com.github.enteraname74.domain.repository.MusicAlbumRepository
import com.github.enteraname74.domain.repository.MusicArtistRepository
import com.github.enteraname74.domain.repository.MusicPlaylistRepository
import com.github.enteraname74.domain.repository.MusicRepository
import com.github.enteraname74.domain.repository.PlaylistRepository
import com.github.soulsearching.events.MusicEvent
import com.github.soulsearching.events.handlers.MusicEventHandler
import com.github.soulsearching.model.PlaybackManager
import com.github.soulsearching.model.settings.SoulSearchingSettings
import com.github.soulsearching.states.MusicState
import com.github.soulsearching.theme.ColorThemeManager
import com.github.soulsearching.types.PlayerMode
import com.github.soulsearching.types.SortDirection
import com.github.soulsearching.types.SortType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Handler for managing the PlayerViewModel.
 */
class PlayerViewModelHandler(
    musicRepository: MusicRepository,
    playlistRepository: PlaylistRepository,
    musicPlaylistRepository: MusicPlaylistRepository,
    albumRepository: AlbumRepository,
    artistRepository: ArtistRepository,
    musicAlbumRepository: MusicAlbumRepository,
    musicArtistRepository: MusicArtistRepository,
    albumArtistRepository: AlbumArtistRepository,
    imageCoverRepository: ImageCoverRepository,
    private val settings: SoulSearchingSettings,
    private val playbackManager: PlaybackManager,
    private val colorThemeManager: ColorThemeManager
): ViewModelHandler {
    private val _musicState = MutableStateFlow(MusicState())
    val musicState = _musicState.asStateFlow()
    private val _sortType = MutableStateFlow(SortType.NAME)
    private val _sortDirection = MutableStateFlow(SortDirection.ASC)

    var currentMusic by mutableStateOf<Music?>(null)
    var currentMusicPosition by mutableIntStateOf(0)
    var currentMusicCover by mutableStateOf<ImageBitmap?>(null)

    var currentPlaylist by mutableStateOf<List<Music>>(emptyList())
    var isPlaying by mutableStateOf(false)
    var playerMode by mutableStateOf(PlayerMode.NORMAL)

    private val musicEventHandler = MusicEventHandler(
        privateState = _musicState,
        publicState = musicState,
        musicRepository = musicRepository,
        playlistRepository = playlistRepository,
        albumRepository = albumRepository,
        artistRepository = artistRepository,
        musicPlaylistRepository = musicPlaylistRepository,
        musicAlbumRepository = musicAlbumRepository,
        musicArtistRepository = musicArtistRepository,
        albumArtistRepository = albumArtistRepository,
        imageCoverRepository = imageCoverRepository,
        sortDirection = _sortDirection,
        sortType = _sortType,
        settings = settings,
        playbackManager = playbackManager
    )


    /**
     * Manage music events.
     */
    fun onMusicEvent(event: MusicEvent) {
        musicEventHandler.handleEvent(event)
    }
}