package com.github.enteraname74.soulsearching.di

import android.content.Context
import com.github.enteraname74.domain.model.SoulSearchingSettings
import com.github.enteraname74.soulsearching.domain.model.MusicFetcher
import com.github.enteraname74.soulsearching.domain.model.settings.SoulSearchingSettingsImpl
import com.github.enteraname74.soulsearching.feature.mainpage.domain.viewmodel.AllMusicsViewModel
import com.github.enteraname74.soulsearching.feature.player.domain.model.PlaybackManager
import com.github.enteraname74.soulsearching.model.playback.PlaybackManagerAndroidImpl
import com.github.enteraname74.soulsearching.model.utils.MusicFetcherAndroidImpl
import com.github.enteraname74.soulsearching.viewmodel.AllMusicsViewModelAndroidImpl
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::AllMusicsViewModelAndroidImpl) bind AllMusicsViewModel::class
    singleOf(::MusicFetcherAndroidImpl) bind MusicFetcher::class
    singleOf(::PlaybackManagerAndroidImpl) bind PlaybackManager::class
    single<SoulSearchingSettings> {
        SoulSearchingSettingsImpl(
            settings = SharedPreferencesSettings(
                delegate = androidApplication().getSharedPreferences(
                    SoulSearchingSettings.SHARED_PREF_KEY,
                    Context.MODE_PRIVATE
                )
            )
        )
    }
}