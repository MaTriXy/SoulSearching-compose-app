package com.github.soulsearching.model

import com.github.enteraname74.domain.repository.MusicRepository
import com.github.enteraname74.domain.repository.PlayerMusicRepository
import com.github.soulsearching.domain.model.settings.SoulSearchingSettings
import com.github.soulsearching.player.domain.model.PlaybackManager
import com.github.soulsearching.player.domain.model.SoulSearchingPlayer

/**
 * Class managing playback on desktop.
 */
class PlaybackManagerDesktopImpl(
    settings: SoulSearchingSettings,
    playerMusicRepository: PlayerMusicRepository,
    musicRepository: MusicRepository
): PlaybackManager(
    settings = settings,
    playerMusicRepository = playerMusicRepository,
    musicRepository = musicRepository
) {
    override val player: SoulSearchingPlayer
        get() = TODO("Not yet implemented")

    override fun stopPlayback() {
        TODO("Not yet implemented")
    }

    override fun update() {
        TODO("Not yet implemented")
    }
}