package com.github.soulsearching

import com.github.soulsearching.colortheme.domain.model.ColorThemeManager
import com.github.soulsearching.domain.model.MusicFetcher
import com.github.soulsearching.domain.model.settings.SoulSearchingSettings
import com.github.soulsearching.domain.model.settings.SoulSearchingSettingsImpl
import com.github.soulsearching.model.MusicFetcherDesktopImpl
import com.github.soulsearching.model.PlaybackManagerDesktopImpl
import com.github.soulsearching.settings.domain.ViewSettingsManager
import com.github.soulsearching.viewmodel.AllAlbumsViewModelDesktopImpl
import com.github.soulsearching.viewmodel.AllArtistsViewModelDesktopImpl
import com.github.soulsearching.viewmodel.AllImageCoversViewModelDesktopImpl
import com.github.soulsearching.viewmodel.AllMusicsViewModelDesktopImpl
import com.github.soulsearching.viewmodel.AllPlaylistsViewModelDesktopImpl
import com.github.soulsearching.viewmodel.AllQuickAccessViewModelDesktopImpl
import com.github.soulsearching.viewmodel.MainActivityViewModelDesktopImpl
import com.github.soulsearching.viewmodel.ModifyAlbumViewModelDesktopImpl
import com.github.soulsearching.viewmodel.ModifyArtistViewModelDesktopImpl
import com.github.soulsearching.viewmodel.ModifyMusicViewModelDesktopImpl
import com.github.soulsearching.viewmodel.ModifyPlaylistViewModelDesktopImpl
import com.github.soulsearching.viewmodel.NavigationViewModelDesktopImpl
import com.github.soulsearching.viewmodel.PlayerViewModelDesktopImpl
import com.github.soulsearching.viewmodel.SelectedAlbumViewModelDesktopImpl
import com.github.soulsearching.viewmodel.SelectedArtistViewModelDesktopImpl
import com.github.soulsearching.viewmodel.SelectedFolderViewModelDesktopImpl
import com.github.soulsearching.viewmodel.SelectedMonthViewModelDesktopImpl
import com.github.soulsearching.viewmodel.SelectedPlaylistViewModelDesktopImpl
import com.github.soulsearching.viewmodel.SettingsAddMusicsViewModelDesktopImpl
import com.github.soulsearching.viewmodel.SettingsAllFoldersViewModelDesktopImpl
import com.russhwolf.settings.PreferencesSettings
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.prefs.Preferences

actual val appModule: Module = module {
    single {
        SettingsAddMusicsViewModelDesktopImpl(
            folderRepository = get(),
            musicRepository = get(),
            musicFetcher = get()
        )
    }
    single {
        AllAlbumsViewModelDesktopImpl(
            albumRepository = get(),
            settings = get()
        )
    }
    single {
        AllArtistsViewModelDesktopImpl(
            artistRepository = get(),
            musicRepository = get(),
            albumRepository = get(),
            settings = get()
        )
    }
    single {
        SettingsAllFoldersViewModelDesktopImpl(
            folderRepository = get(),
            musicRepository = get()
        )
    }
    single {
        AllImageCoversViewModelDesktopImpl(
            imageCoverRepository = get(),
            musicRepository = get(),
            albumRepository = get(),
            artistRepository = get(),
            playlistRepository = get()
        )
    }
    single {
        AllMusicsViewModelDesktopImpl(
            musicRepository = get(),
            playlistRepository = get(),
            musicAlbumRepository = get(),
            musicArtistRepository = get(),
            settings = get(),
            playbackManager = get(),
            musicFetcher = get()
        )
    }
    single {
        AllPlaylistsViewModelDesktopImpl(
            playlistRepository = get(),
            musicPlaylistRepository = get(),
            settings = get(),
            musicRepository = get(),
            playbackManager = get()
        )
    }
    single {
        AllQuickAccessViewModelDesktopImpl(
            musicRepository = get(),
            playlistRepository = get(),
            albumRepository = get(),
            artistRepository = get()
        )
    }
    single {
        MainActivityViewModelDesktopImpl(
            settings = get()
        )
    }
    single {
        ModifyAlbumViewModelDesktopImpl(
            albumRepository = get(),
            artistRepository = get(),
            imageCoverRepository = get(),
            playbackManager = get()
        )
    }
    single {
        ModifyArtistViewModelDesktopImpl(
            artistRepository = get(),
            imageCoverRepository = get()
        )
    }
    single {
        ModifyMusicViewModelDesktopImpl(
            musicRepository = get(),
            artistRepository = get(),
            albumRepository = get(),
            imageCoverRepository = get(),
            playbackManager = get()
        )
    }
    single {
        ModifyPlaylistViewModelDesktopImpl(
            playlistRepository = get(),
            imageCoverRepository = get()
        )
    }
    single {
        PlayerViewModelDesktopImpl(
            musicRepository = get(),
            playbackManager = get(),
            colorThemeManager = get(),
            musicPlaylistRepository = get(),
            playlistRepository = get(),
            lyricsProvider = get()
        )
    }
    single {
        SelectedAlbumViewModelDesktopImpl(
            albumRepository = get(),
            musicRepository = get(),
            playlistRepository = get(),
            musicPlaylistRepository = get(),
            playbackManager = get()
        )
    }
    single {
        SelectedArtistViewModelDesktopImpl(
            playlistRepository = get(),
            musicRepository = get(),
            artistRepository = get(),
            musicPlaylistRepository = get(),
            playbackManager = get(),
            albumRepository = get()
        )
    }
    single {
        SelectedPlaylistViewModelDesktopImpl(
            playlistRepository = get(),
            musicRepository = get(),
            musicPlaylistRepository = get(),
            playbackManager = get()
        )
    }
    single {
        SelectedFolderViewModelDesktopImpl(
            playbackManager = get(),
            playlistRepository = get(),
            musicPlaylistRepository = get(),
            musicRepository = get()
        )
    }
    single {
        SelectedMonthViewModelDesktopImpl(
            playbackManager = get(),
            playlistRepository = get(),
            musicPlaylistRepository = get(),
            musicRepository = get()
        )
    }
    single {
        NavigationViewModelDesktopImpl()
    }
    factory<MusicFetcher> {
        MusicFetcherDesktopImpl(
            playlistRepository = get(),
            musicRepository = get(),
            albumRepository = get(),
            artistRepository = get(),
            musicAlbumRepository = get(),
            musicArtistRepository = get(),
            albumArtistRepository = get(),
            imageCoverRepository = get(),
            folderRepository = get()
        )
    }
    factory<SoulSearchingSettings> {
        SoulSearchingSettingsImpl(
            settings = PreferencesSettings(
                delegate = Preferences.userRoot()
            )
        )
    }
    single<PlaybackManagerDesktopImpl> {
        PlaybackManagerDesktopImpl(
            settings = get(),
            playerMusicRepository = get(),
            musicRepository = get()
        )
    }
    single<MusicFetcherDesktopImpl> {
        MusicFetcherDesktopImpl(
            playlistRepository = get(),
            musicRepository = get(),
            albumRepository = get(),
            artistRepository = get(),
            musicAlbumRepository = get(),
            musicArtistRepository = get(),
            albumArtistRepository = get(),
            imageCoverRepository = get(),
            folderRepository = get()
        )
    }
    single<ColorThemeManager> {
        ColorThemeManager(
            settings = get()
        )
    }
    single<ViewSettingsManager> {
        ViewSettingsManager(
            settings = get()
        )
    }
}