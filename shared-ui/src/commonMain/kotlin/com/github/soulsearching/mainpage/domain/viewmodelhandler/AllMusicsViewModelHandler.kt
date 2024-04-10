package com.github.soulsearching.mainpage.domain.viewmodelhandler

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.enteraname74.domain.model.Music
import com.github.enteraname74.domain.repository.MusicAlbumRepository
import com.github.enteraname74.domain.repository.MusicArtistRepository
import com.github.enteraname74.domain.repository.MusicRepository
import com.github.enteraname74.domain.repository.PlaylistRepository
import com.github.soulsearching.domain.events.MusicEvent
import com.github.soulsearching.domain.events.handlers.MusicEventHandler
import com.github.soulsearching.domain.model.MusicFetcher
import com.github.soulsearching.domain.model.settings.SoulSearchingSettings
import com.github.soulsearching.domain.model.types.BottomSheetStates
import com.github.soulsearching.mainpage.domain.state.MainPageState
import com.github.soulsearching.mainpage.domain.model.ElementEnum
import com.github.soulsearching.mainpage.domain.model.SortDirection
import com.github.soulsearching.mainpage.domain.model.SortType
import com.github.soulsearching.domain.viewmodel.handler.ViewModelHandler
import com.github.soulsearching.player.domain.model.PlaybackManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import java.util.UUID
import kotlin.math.max

/**
 * Handler for managing the AllMusicsViewModel.
 */
abstract class AllMusicsViewModelHandler(
    coroutineScope: CoroutineScope,
    private val musicRepository: MusicRepository,
    private val musicAlbumRepository: MusicAlbumRepository,
    private val musicArtistRepository: MusicArtistRepository,
    playlistRepository: PlaylistRepository,
    settings: SoulSearchingSettings,
    playbackManager: PlaybackManager,
    private val musicFetcher: MusicFetcher,
): ViewModelHandler {
    var currentPage by mutableStateOf<ElementEnum?>(null)
    private val _sortType = MutableStateFlow(SortType.ADDED_DATE)
    private val _sortDirection = MutableStateFlow(SortDirection.ASC)

    @OptIn(ExperimentalMaterialApi::class)
    val searchDraggableState: SwipeableState<BottomSheetStates> = SwipeableState(initialValue = BottomSheetStates.COLLAPSED)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _musics = _sortDirection.flatMapLatest { sortDirection ->
        _sortType.flatMapLatest { sortType ->
            when (sortDirection) {
                SortDirection.ASC -> {
                    when (sortType) {
                        SortType.NAME -> musicRepository.getAllMusicsSortByNameAscAsFlow()
                        SortType.ADDED_DATE -> musicRepository.getAllMusicsSortByAddedDateAscAsFlow()
                        SortType.NB_PLAYED -> musicRepository.getAllMusicsSortByNbPlayedAscAsFlow()
                        else -> musicRepository.getAllMusicsSortByNameAscAsFlow()
                    }
                }
                SortDirection.DESC -> {
                    when (sortType) {
                        SortType.NAME -> musicRepository.getAllMusicsSortByNameDescAsFlow()
                        SortType.ADDED_DATE -> musicRepository.getAllMusicsSortByAddedDateDescAsFlow()
                        SortType.NB_PLAYED -> musicRepository.getAllMusicsSortByNbPlayedDescAsFlow()
                        else -> musicRepository.getAllMusicsSortByNameDescAsFlow()
                    }
                }
                else -> musicRepository.getAllMusicsSortByNameAscAsFlow()
            }
        }
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(),
        ArrayList()
    )

    private val _playlists = playlistRepository.getAllPlaylistsWithMusicsSortByNameAscAsFlow()
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )


    private val _state = MutableStateFlow(MainPageState())

    val state = combine(
        _state,
        _musics,
        _sortType,
        _sortDirection,
        _playlists
    ) { state, musics, sortType, sortDirection, playlists ->
        state.copy(
            musics = musics as ArrayList<Music>,
            sortType = sortType,
            sortDirection = sortDirection,
            allPlaylists = playlists
        )
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(5000),
        MainPageState()
    )

    private val musicEventHandler = MusicEventHandler(
        privateState = _state,
        musicRepository = musicRepository,
        sortType = _sortType,
        sortDirection = _sortDirection,
        settings = settings,
        playbackManager = playbackManager
    )

    /**
     * Retrieve the current page index.
     * If nothing is set, it will return 0 by default.
     */
    fun getCurrentPage(visibleElements: List<ElementEnum>): Int {
        return if (currentPage == null) {
            0
        } else {
            max(0, visibleElements.indexOf(currentPage))
        }
    }

    /**
     * Check if a music is in the favorites.
     */
    suspend fun isMusicInFavorite(musicId: UUID): Boolean {
        return musicRepository.getMusicFromFavoritePlaylist(musicId = musicId) != null
    }

    /**
     * Retrieve the artist id of a music.
     */
    fun getArtistIdFromMusicId(musicId: UUID): UUID? {
        return runBlocking(context = Dispatchers.IO) { musicArtistRepository.getArtistIdFromMusicId(musicId) }
    }

    /**
     * Retrieve the album id of a music.
     */
    fun getAlbumIdFromMusicId(musicId: UUID): UUID? {
        return runBlocking(context = Dispatchers.IO) { musicAlbumRepository.getAlbumIdFromMusicId(musicId) }
    }

    /**
     * Fetch all musics.
     */
    suspend fun fetchMusics(
        updateProgress: (Float) -> Unit,
        finishAction: () -> Unit
    ) {
        musicFetcher.fetchMusics(
            updateProgress = updateProgress,
            finishAction = finishAction
        )
    }

    /**
     * Check all musics and delete the one that does not exists (if the path of the music is not valid).
     */
    abstract fun checkAndDeleteMusicIfNotExist()

    /**
     * Manage music events.
     */
    fun onMusicEvent(event: MusicEvent) {
        musicEventHandler.handleEvent(event)
    }
}