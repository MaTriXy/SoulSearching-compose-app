package com.github.soulsearching

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.soulsearching.classes.PlayerUtils
import com.github.soulsearching.classes.SettingsUtils
import com.github.soulsearching.classes.SharedPrefUtils
import com.github.soulsearching.classes.Utils
import com.github.soulsearching.classes.enumsAndTypes.BottomSheetStates
import com.github.soulsearching.composables.FetchingMusicsComposable
import com.github.soulsearching.composables.MissingPermissionsComposable
import com.github.soulsearching.composables.bottomSheets.PlayerDraggableView
import com.github.soulsearching.composables.bottomSheets.PlayerMusicListView
import com.github.soulsearching.composables.remembercomposable.rememberPlayerDraggableState
import com.github.soulsearching.composables.remembercomposable.rememberPlayerMusicDraggableState
import com.github.soulsearching.composables.remembercomposable.rememberPostNotificationGranted
import com.github.soulsearching.composables.remembercomposable.rememberReadPermissionGranted
import com.github.soulsearching.composables.remembercomposable.rememberSearchDraggableState
import com.github.soulsearching.events.AddMusicsEvent
import com.github.soulsearching.events.FolderEvent
import com.github.soulsearching.events.MusicEvent
import com.github.soulsearching.events.PlaylistEvent
import com.github.soulsearching.screens.MainPageScreen
import com.github.soulsearching.screens.ModifyAlbumScreen
import com.github.soulsearching.screens.ModifyArtistScreen
import com.github.soulsearching.screens.ModifyMusicScreen
import com.github.soulsearching.screens.ModifyPlaylistScreen
import com.github.soulsearching.screens.MoreAlbumsScreen
import com.github.soulsearching.screens.MoreArtistsScreen
import com.github.soulsearching.screens.MorePlaylistsScreen
import com.github.soulsearching.screens.SelectedAlbumScreen
import com.github.soulsearching.screens.SelectedArtistScreen
import com.github.soulsearching.screens.SelectedPlaylistScreen
import com.github.soulsearching.screens.settings.SettingsAboutScreen
import com.github.soulsearching.screens.settings.SettingsAddMusicsScreen
import com.github.soulsearching.screens.settings.SettingsColorThemeScreen
import com.github.soulsearching.screens.settings.SettingsDevelopersScreen
import com.github.soulsearching.screens.settings.SettingsManageMusicsScreen
import com.github.soulsearching.screens.settings.SettingsPersonalisationScreen
import com.github.soulsearching.screens.settings.SettingsScreen
import com.github.soulsearching.screens.settings.SettingsUsedFoldersScreen
import com.github.soulsearching.service.PlayerService
import com.github.soulsearching.ui.theme.DynamicColor
import com.github.soulsearching.ui.theme.SoulSearchingTheme
import com.github.soulsearching.viewModels.AddMusicsViewModel
import com.github.soulsearching.viewModels.AllAlbumsViewModel
import com.github.soulsearching.viewModels.AllArtistsViewModel
import com.github.soulsearching.viewModels.AllFoldersViewModel
import com.github.soulsearching.viewModels.AllImageCoversViewModel
import com.github.soulsearching.viewModels.AllMusicsViewModel
import com.github.soulsearching.viewModels.AllPlaylistsViewModel
import com.github.soulsearching.viewModels.AllQuickAccessViewModel
import com.github.soulsearching.viewModels.ModifyAlbumViewModel
import com.github.soulsearching.viewModels.ModifyArtistViewModel
import com.github.soulsearching.viewModels.ModifyMusicViewModel
import com.github.soulsearching.viewModels.ModifyPlaylistViewModel
import com.github.soulsearching.viewModels.PlayerMusicListViewModel
import com.github.soulsearching.viewModels.PlayerViewModel
import com.github.soulsearching.viewModels.SelectedAlbumViewModel
import com.github.soulsearching.viewModels.SelectedArtistViewModel
import com.github.soulsearching.viewModels.SelectedPlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // Main page view models
    private val allMusicsViewModel: AllMusicsViewModel by viewModels()
    private val allPlaylistsViewModel: AllPlaylistsViewModel by viewModels()
    private val allAlbumsViewModel: AllAlbumsViewModel by viewModels()
    private val allArtistsViewModel: AllArtistsViewModel by viewModels()
    private val allImageCoversViewModel: AllImageCoversViewModel by viewModels()
    private val allQuickAccessViewModel: AllQuickAccessViewModel by viewModels()

    // Selected page view models
    private val selectedPlaylistViewModel: SelectedPlaylistViewModel by viewModels()
    private val selectedAlbumViewModel: SelectedAlbumViewModel by viewModels()
    private val selectedArtistsViewModel: SelectedArtistViewModel by viewModels()

    // Modify page view models
    private val modifyPlaylistViewModel: ModifyPlaylistViewModel by viewModels()
    private val modifyAlbumViewModel: ModifyAlbumViewModel by viewModels()
    private val modifyArtistViewModel: ModifyArtistViewModel by viewModels()
    private val modifyMusicViewModel: ModifyMusicViewModel by viewModels()

    // Player view model :
    private val playerViewModel: PlayerViewModel by viewModels()
    private val playerMusicListViewModel: PlayerMusicListViewModel by viewModels()

    // Settings view models:
    private val allFoldersViewModel: AllFoldersViewModel by viewModels()
    private val addMusicsViewModel: AddMusicsViewModel by viewModels()

    private val serviceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("MAIN ACTIVITY", "SERVICE DIED, WILL RESTART")
            Utils.launchService(
                context = context,
                isFromSavedList = false
            )
        }
    }

    /**
     * Initialize the SharedPreferences.
     */
    private fun initializeSharedPreferences() {
        SharedPrefUtils.sharedPreferences =
            getSharedPreferences(SharedPrefUtils.SHARED_PREF_KEY, Context.MODE_PRIVATE)
        SharedPrefUtils.initializeSorts(
            onMusicEvent = allMusicsViewModel::onMusicEvent,
            onPlaylistEvent = allPlaylistsViewModel::onPlaylistEvent,
            onArtistEvent = allArtistsViewModel::onArtistEvent,
            onAlbumEvent = allAlbumsViewModel::onAlbumEvent
        )
        SharedPrefUtils.initializeSettings()
    }

    /**
     * Initialize the player ViewModel, used for managing view elements related to the playback.
     */
    private fun initializePlayerViewModel() {
        PlayerUtils.playerViewModel = playerViewModel
        PlayerUtils.playerViewModel.retrieveCoverMethod = allImageCoversViewModel::getImageCover
        PlayerUtils.playerViewModel.updateNbPlayed =
            { allMusicsViewModel.onMusicEvent(MusicEvent.AddNbPlayed(it)) }
    }

    /**
     * Initialize the broadcast receiver, used by the foreground service handling the playback.
     */
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun initializeBroadcastReceive() {
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(
                serviceReceiver, IntentFilter(PlayerService.RESTART_SERVICE),
                RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(serviceReceiver, IntentFilter(PlayerService.RESTART_SERVICE))
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition", "UnspecifiedRegisterReceiverFlag")
    @OptIn(ExperimentalFoundationApi::class)
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeSharedPreferences()
        initializePlayerViewModel()
        initializeBroadcastReceive()

        setContent {
            SoulSearchingTheme {
                val playlistState by allPlaylistsViewModel.state.collectAsState()
                val albumState by allAlbumsViewModel.state.collectAsState()
                val artistState by allArtistsViewModel.state.collectAsState()
                val musicState by allMusicsViewModel.state.collectAsState()
                val coversState by allImageCoversViewModel.state.collectAsState()
                val playerMusicListState by playerMusicListViewModel.state.collectAsState()
                val playerMusicState by PlayerUtils.playerViewModel.state.collectAsState()
                val quickAccessState by allQuickAccessViewModel.state.collectAsState()

                val coroutineScope = rememberCoroutineScope()

                var hasLastPlayedMusicsBeenFetched by rememberSaveable { mutableStateOf(false) }
                var cleanImagesLaunched by rememberSaveable { mutableStateOf(false) }
                var cleanMusicsLaunched by rememberSaveable { mutableStateOf(false) }

                var isReadPermissionGranted by rememberReadPermissionGranted()
                var isPostNotificationGranted by rememberPostNotificationGranted()
                var hasMusicsBeenFetched by rememberSaveable {
                    mutableStateOf(SharedPrefUtils.hasMusicsBeenFetched())
                }

                val readPermissionLauncher = permissionLauncher { isGranted ->
                    isReadPermissionGranted = isGranted
                }

                val postNotificationLauncher = permissionLauncher { isGranted ->
                    isPostNotificationGranted = isGranted
                }

                SideEffect {
                    checkAndAskMissingPermissions(
                        isReadPermissionGranted = isReadPermissionGranted,
                        isPostNotificationGranted = isPostNotificationGranted,
                        readPermissionLauncher = readPermissionLauncher,
                        postNotificationLauncher = postNotificationLauncher
                    )
                }

                if (!isReadPermissionGranted || !isPostNotificationGranted) {
                    MissingPermissionsComposable()
                    return@SoulSearchingTheme
                }
                if (!hasMusicsBeenFetched) {
                    FetchingMusicsComposable(
                        finishAddingMusicsAction = {
                            SharedPrefUtils.setMusicsFetched()
                            hasMusicsBeenFetched = true
                        },
                        addingMusicAction = { music, cover ->
                            runBlocking {
                                val job = CoroutineScope(Dispatchers.IO).launch {
                                    allMusicsViewModel.addMusic(music, cover)
                                }
                                job.join()
                            }
                        },
                        createFavoritePlaylistAction = {
                            allPlaylistsViewModel.onPlaylistEvent(
                                PlaylistEvent.AddFavoritePlaylist(
                                    name = applicationContext.getString(R.string.favorite)
                                )
                            )
                        }
                    )
                } else {
                    if (coversState.covers.isNotEmpty() && !cleanImagesLaunched) {
                        LaunchedEffect(key1 = "Launch") {
                            CoroutineScope(Dispatchers.IO).launch {
                                for (cover in coversState.covers) {
                                    allImageCoversViewModel.verifyIfImageIsUsed(cover)
                                }
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                if (PlayerUtils.playerViewModel.currentMusic != null) {
                                    PlayerUtils.playerViewModel.defineCoverAndPaletteFromCoverId(
                                        coverId = PlayerUtils.playerViewModel.currentMusic?.coverId
                                    )
                                    PlayerService.updateNotification()
                                }
                            }
                            cleanImagesLaunched = true
                        }
                    }

                    if (musicState.musics.isNotEmpty() && !cleanMusicsLaunched) {
                        allMusicsViewModel.checkAndDeleteMusicIfNotExist(applicationContext)
                        cleanMusicsLaunched = true
                    }

                    if (PlayerUtils.playerViewModel.shouldServiceBeLaunched && !PlayerUtils.playerViewModel.isServiceLaunched) {
                        Utils.launchService(
                            context = this@MainActivity,
                            isFromSavedList = false
                        )
                    }

                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = DynamicColor.primary)
                    ) {
                        val navController = rememberNavController()
                        val constraintsScope = this
                        val maxHeight = with(LocalDensity.current) {
                            constraintsScope.maxHeight.toPx()
                        }

                        val playerDraggableState = rememberPlayerDraggableState(
                            constraintsScope = constraintsScope
                        )

                        val musicListDraggableState = rememberPlayerMusicDraggableState(
                            constraintsScope = constraintsScope
                        )
                        val searchDraggableState = rememberSearchDraggableState(
                            constraintsScope = constraintsScope
                        )


                        if (!hasLastPlayedMusicsBeenFetched) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val playerSavedMusics =
                                    playerMusicListViewModel.getPlayerMusicList()
                                if (playerSavedMusics.isNotEmpty()) {
                                    PlayerUtils.playerViewModel.setPlayerInformationFromSavedList(
                                        playerSavedMusics
                                    )
                                    Utils.launchService(
                                        context = this@MainActivity,
                                        isFromSavedList = true
                                    )
                                    PlayerUtils.playerViewModel.shouldServiceBeLaunched = true
                                    coroutineScope.launch {
                                        playerDraggableState.state.animateTo(BottomSheetStates.MINIMISED)
                                    }
                                }
                            }
                            hasLastPlayedMusicsBeenFetched = true
                        }

                        NavHost(navController = navController, startDestination = "mainPage") {
                            composable("mainPage") {
                                MainPageScreen(
                                    allMusicsViewModel = allMusicsViewModel,
                                    allPlaylistsViewModel = allPlaylistsViewModel,
                                    allAlbumsViewModel = allAlbumsViewModel,
                                    allArtistsViewModel = allArtistsViewModel,
                                    allImageCoversViewModel = allImageCoversViewModel,
                                    playerMusicListViewModel = playerMusicListViewModel,
                                    navigateToPlaylist = {
                                        navController.navigate("selectedPlaylist/$it")
                                    },
                                    navigateToAlbum = {
                                        navController.navigate("selectedAlbum/$it")
                                    },
                                    navigateToArtist = {
                                        navController.navigate("selectedArtist/$it")
                                    },
                                    navigateToMorePlaylist = {
                                        navController.navigate("morePlaylists")
                                    },
                                    navigateToMoreArtists = {
                                        navController.navigate("moreArtists")
                                    },
                                    navigateToMoreShortcuts = {
                                        navController.navigate("moreShortcuts")
                                    },
                                    navigateToMoreAlbums = {
                                        navController.navigate("moreAlbums")
                                    },
                                    navigateToModifyMusic = {
                                        navController.navigate("modifyMusic/$it")
                                    },
                                    navigateToModifyPlaylist = {
                                        navController.navigate("modifyPlaylist/$it")
                                    },
                                    navigateToModifyAlbum = {
                                        navController.navigate("modifyAlbum/$it")
                                    },
                                    navigateToModifyArtist = {
                                        navController.navigate("modifyArtist/$it")
                                    },
                                    navigateToSettings = {
                                        navController.navigate("settings")
                                    },
                                    playerDraggableState = playerDraggableState,
                                    searchDraggableState = searchDraggableState,
                                    musicState = musicState,
                                    playlistState = playlistState,
                                    albumState = albumState,
                                    artistState = artistState,
                                    quickAccessState = quickAccessState
                                )
                            }
                            composable(
                                "selectedPlaylist/{playlistId}",
                                arguments = listOf(navArgument("playlistId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                SelectedPlaylistScreen(
                                    selectedPlaylistViewModel = selectedPlaylistViewModel,
                                    navigateToModifyPlaylist = {
                                        navController.navigate(
                                            "modifyPlaylist/" + backStackEntry.arguments?.getString(
                                                "playlistId"
                                            )
                                        )
                                    },
                                    selectedPlaylistId = backStackEntry.arguments?.getString("playlistId")!!,
                                    playlistState = playlistState,
                                    onPlaylistEvent = allPlaylistsViewModel::onPlaylistEvent,
                                    navigateToModifyMusic = { navController.navigate("modifyMusic/$it") },
                                    navigateBack = {
                                        SettingsUtils.settingsViewModel.setPlaylistColorPalette(
                                            null
                                        )
                                        SettingsUtils.settingsViewModel.forceBasicThemeForPlaylists =
                                            false
                                        navController.popBackStack()
                                    },
                                    retrieveCoverMethod = {
                                        allImageCoversViewModel.getImageCover(
                                            it
                                        )
                                    },
                                    playerDraggableState = playerDraggableState,
                                    playerMusicListViewModel = playerMusicListViewModel
                                )
                            }
                            composable(
                                "selectedAlbum/{albumId}",
                                arguments = listOf(navArgument("albumId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                SelectedAlbumScreen(
                                    selectedAlbumViewModel = selectedAlbumViewModel,
                                    navigateToModifyAlbum = {
                                        navController.navigate(
                                            "modifyAlbum/" + backStackEntry.arguments?.getString(
                                                "albumId"
                                            )
                                        )
                                    },
                                    selectedAlbumId = backStackEntry.arguments?.getString("albumId")!!,
                                    playlistState = playlistState,
                                    onPlaylistEvent = allPlaylistsViewModel::onPlaylistEvent,
                                    navigateToModifyMusic = { navController.navigate("modifyMusic/$it") },
                                    navigateBack = {
                                        SettingsUtils.settingsViewModel.setPlaylistColorPalette(
                                            null
                                        )
                                        SettingsUtils.settingsViewModel.forceBasicThemeForPlaylists =
                                            false
                                        navController.popBackStack()
                                    },
                                    retrieveCoverMethod = {
                                        allImageCoversViewModel.getImageCover(
                                            it
                                        )
                                    },
                                    playerDraggableState = playerDraggableState,
                                    playerMusicListViewModel = playerMusicListViewModel
                                )
                            }
                            composable(
                                "selectedArtist/{artistId}",
                                arguments = listOf(navArgument("artistId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                SelectedArtistScreen(
                                    selectedArtistViewModel = selectedArtistsViewModel,
                                    navigateToModifyArtist = {
                                        navController.navigate(
                                            "modifyArtist/" + backStackEntry.arguments?.getString(
                                                "artistId"
                                            )
                                        )
                                    },
                                    selectedArtistId = backStackEntry.arguments?.getString("artistId")!!,
                                    playlistState = playlistState,
                                    onPlaylistEvent = allPlaylistsViewModel::onPlaylistEvent,
                                    navigateToModifyMusic = { navController.navigate("modifyMusic/$it") },
                                    navigateBack = {
                                        SettingsUtils.settingsViewModel.setPlaylistColorPalette(
                                            null
                                        )
                                        SettingsUtils.settingsViewModel.forceBasicThemeForPlaylists =
                                            false
                                        navController.popBackStack()
                                    },
                                    retrieveCoverMethod = {
                                        allImageCoversViewModel.getImageCover(
                                            it
                                        )
                                    },
                                    playerDraggableState = playerDraggableState,
                                    playerMusicListViewModel = playerMusicListViewModel
                                )
                            }
                            composable(
                                "modifyPlaylist/{playlistId}",
                                arguments = listOf(navArgument("playlistId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                ModifyPlaylistScreen(
                                    modifyPlaylistViewModel = modifyPlaylistViewModel,
                                    selectedPlaylistId = backStackEntry.arguments?.getString("playlistId")!!,
                                    finishAction = { navController.popBackStack() }
                                )
                            }
                            composable(
                                "modifyMusic/{musicId}",
                                arguments = listOf(navArgument("musicId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                ModifyMusicScreen(
                                    modifyMusicViewModel = modifyMusicViewModel,
                                    selectedMusicId = backStackEntry.arguments?.getString("musicId")!!,
                                    finishAction = { navController.popBackStack() }
                                )
                            }
                            composable(
                                "modifyAlbum/{albumId}",
                                arguments = listOf(navArgument("albumId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                ModifyAlbumScreen(
                                    modifyAlbumViewModel = modifyAlbumViewModel,
                                    selectedAlbumId = backStackEntry.arguments?.getString("albumId")!!,
                                    finishAction = { navController.popBackStack() }
                                )
                            }
                            composable(
                                "modifyArtist/{artistId}",
                                arguments = listOf(navArgument("artistId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                ModifyArtistScreen(
                                    modifyArtistViewModel = modifyArtistViewModel,
                                    selectedArtistId = backStackEntry.arguments?.getString("artistId")!!,
                                    finishAction = { navController.popBackStack() }
                                )
                            }
                            composable(
                                "morePlaylists"
                            ) {
                                MorePlaylistsScreen(
                                    allPlaylistsViewModel = allPlaylistsViewModel,
                                    navigateToSelectedPlaylist = { navController.navigate("selectedPlaylist/$it") },
                                    finishAction = { navController.popBackStack() },
                                    navigateToModifyPlaylist = { navController.navigate("modifyPlaylist/$it") },
                                    retrieveCoverMethod = {
                                        allImageCoversViewModel.getImageCover(
                                            it
                                        )
                                    }
                                )
                            }
                            composable(
                                "moreAlbums"
                            ) {
                                MoreAlbumsScreen(
                                    allAlbumsViewModel = allAlbumsViewModel,
                                    navigateToSelectedAlbum = { navController.navigate("selectedAlbum/$it") },
                                    finishAction = { navController.popBackStack() },
                                    navigateToModifyAlbum = { navController.navigate("modifyAlbum/$it") },
                                    retrieveCoverMethod = {
                                        allImageCoversViewModel.getImageCover(
                                            it
                                        )
                                    }
                                )
                            }
                            composable(
                                "moreArtists"
                            ) {
                                MoreArtistsScreen(
                                    allArtistsViewModel = allArtistsViewModel,
                                    navigateToSelectedArtist = { navController.navigate("selectedArtist/$it") },
                                    finishAction = { navController.popBackStack() },
                                    navigateToModifyArtist = { navController.navigate("modifyArtist/$it") },
                                    retrieveCoverMethod = {
                                        allImageCoversViewModel.getImageCover(
                                            it
                                        )
                                    }
                                )
                            }
                            composable(
                                "settings"
                            ) {
                                SettingsScreen(
                                    finishAction = { navController.popBackStack() },
                                    navigateToColorTheme = {
                                        navController.navigate("colorTheme")
                                    },
                                    navigateToManageMusics = {
                                        navController.navigate("manageMusics")
                                    },
                                    navigateToPersonalisation = {
                                        navController.navigate("personalisation")
                                    },
                                    navigateToAbout = {
                                        navController.navigate("about")
                                    }
                                )
                            }
                            composable(
                                "personalisation"
                            ) {
                                SettingsPersonalisationScreen(
                                    finishAction = { navController.popBackStack() }
                                )
                            }
                            composable(
                                "manageMusics"
                            ) {
                                SettingsManageMusicsScreen(
                                    finishAction = { navController.popBackStack() },
                                    navigateToFolders = {
                                        allFoldersViewModel.onFolderEvent(
                                            FolderEvent.FetchFolders
                                        )
                                        navController.navigate("usedFolders")
                                    },
                                    navigateToAddMusics = {
                                        addMusicsViewModel.onAddMusicEvent(AddMusicsEvent.ResetState)
                                        navController.navigate("addMusics")
                                    }
                                )
                            }
                            composable(
                                "usedFolders"
                            ) {
                                SettingsUsedFoldersScreen(
                                    finishAction = { navController.popBackStack() },
                                    allFoldersViewModel = allFoldersViewModel
                                )
                            }
                            composable(
                                "addMusics"
                            ) {
                                SettingsAddMusicsScreen(
                                    addMusicsViewModel = addMusicsViewModel,
                                    finishAction = { navController.popBackStack() },
                                    saveMusicFunction = allMusicsViewModel::addMusic
                                )
                            }
                            composable(
                                "colorTheme"
                            ) {
                                SettingsColorThemeScreen(
                                    finishAction = { navController.popBackStack() }
                                )
                            }
                            composable(
                                "about"
                            ) {
                                SettingsAboutScreen(
                                    finishAction = { navController.popBackStack() },
                                    navigateToDevelopers = { navController.navigate("developers") }
                                )
                            }
                            composable(
                                "developers"
                            ) {
                                SettingsDevelopersScreen(
                                    finishAction = { navController.popBackStack() }
                                )
                            }
                        }
                        PlayerDraggableView(
                            maxHeight = maxHeight,
                            draggableState = playerDraggableState,
                            retrieveCoverMethod = allImageCoversViewModel::getImageCover,
                            musicListDraggableState = musicListDraggableState,
                            playerMusicListViewModel = playerMusicListViewModel,
                            onMusicEvent = playerViewModel::onMusicEvent,
                            isMusicInFavoriteMethod = allMusicsViewModel::isMusicInFavorite,
                            navigateToArtist = {
                                navController.navigate("selectedArtist/$it")
                            },
                            navigateToAlbum = {
                                navController.navigate("selectedAlbum/$it")
                            },
                            retrieveAlbumIdMethod = {
                                allMusicsViewModel.getAlbumIdFromMusicId(it)
                            },
                            retrieveArtistIdMethod = {
                                allMusicsViewModel.getArtistIdFromMusicId(it)
                            },
                            musicState = playerMusicState,
                            playlistState = playlistState,
                            onPlaylistEvent = allPlaylistsViewModel::onPlaylistEvent,
                            navigateToModifyMusic = {
                                navController.navigate("modifyMusic/$it")
                            }
                        )

                        PlayerMusicListView(
                            coverList = coversState.covers,
                            musicState = playerMusicListState,
                            playlistState = playlistState,
                            onMusicEvent = playerMusicListViewModel::onMusicEvent,
                            onPlaylistEvent = allPlaylistsViewModel::onPlaylistEvent,
                            navigateToModifyMusic = {
                                navController.navigate("modifyMusic/$it")
                            },
                            musicListDraggableState = musicListDraggableState,
                            playerDraggableState = playerDraggableState,
                            playerMusicListViewModel = playerMusicListViewModel
                        )
                    }
                }
            }
        }
    }

    /**
     * Build a permission launcher.
     */
    @Composable
    private fun permissionLauncher(
        onResult: (Boolean) -> Unit
    ): ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            onResult(isGranted)
        }
    }

    /**
     * Check and ask for missing permissions.
     */
    private fun checkAndAskMissingPermissions(
        isReadPermissionGranted: Boolean,
        isPostNotificationGranted: Boolean,
        readPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
        postNotificationLauncher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        if (!isReadPermissionGranted) {
            readPermissionLauncher.launch(
                if (Build.VERSION.SDK_INT >= 33) {
                    android.Manifest.permission.READ_MEDIA_AUDIO
                } else {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
        }

        if (!isPostNotificationGranted && (Build.VERSION.SDK_INT >= 33)) {
            postNotificationLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onResume() {
        super.onResume()
        allMusicsViewModel.checkAndDeleteMusicIfNotExist(applicationContext)
    }
}