package com.github.soulsearching

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.github.soulsearching.composables.screens.PlaylistScreen
import com.github.soulsearching.ui.theme.SoulSearchingTheme
import com.github.soulsearching.viewModels.SelectedPlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SelectedPlaylistActivity : ComponentActivity() {
    private val viewModel : SelectedPlaylistViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoulSearchingTheme {
                val playlistState by viewModel.playlistState.collectAsState()
                val musicState by viewModel.musicState.collectAsState()

                var isPlaylistFetched by rememberSaveable {
                    mutableStateOf(false)
                }
                if (!isPlaylistFetched) {
                    viewModel.setSelectedPlaylist(UUID.fromString(intent.extras?.getString("playlistId")))
                    isPlaylistFetched = true
                }

                PlaylistScreen(
                    onMusicEvent = viewModel::onMusicEvent,
                    musicState = musicState,
                    title = playlistState.playlistWithMusics.playlist.name,
                    image = playlistState.playlistWithMusics.playlist.playlistCover,
                    editAction = {
                        val intent = Intent(applicationContext, ModifyPlaylistActivity::class.java)
                        intent.putExtra(
                            "playlistId",
                            playlistState.playlistWithMusics.playlist.playlistId.toString()
                        )
                        startActivity(intent)
                    }
                )
            }
        }
    }
}