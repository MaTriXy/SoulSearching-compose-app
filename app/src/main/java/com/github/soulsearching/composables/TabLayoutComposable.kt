package com.github.soulsearching.composables

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.soulsearching.classes.TabRowItem
import com.github.soulsearching.composables.tabLayoutScreens.MusicsScreen
import com.github.soulsearching.composables.tabLayoutScreens.PlaylistsScreen
import com.google.accompanist.pager.rememberPagerState
import com.github.soulsearching.R
import com.github.soulsearching.database.AppDatabase
import com.github.soulsearching.events.MusicEvent
import com.github.soulsearching.events.PlaylistEvent
import com.github.soulsearching.states.MusicState
import com.github.soulsearching.states.PlaylistState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayoutComposable(
    musicState: MusicState,
    playlistsState: PlaylistState,
    onMusicEvent: (MusicEvent) -> Unit,
    onPlaylistEvent: (PlaylistEvent) -> Unit
) {
    val tabRowItems = listOf(
        TabRowItem(
            title = stringResource(R.string.musics),
            screen = {
                MusicsScreen(
                    state = musicState,
                    onEvent = onMusicEvent
                )
            }
        ),
        TabRowItem(
            title = stringResource(R.string.playlists),
            screen = { PlaylistsScreen(state = playlistsState, onEvent = onPlaylistEvent) }
        ),
        TabRowItem(
            title = stringResource(R.string.albums),
            screen = { PlaylistsScreen(state = playlistsState, onEvent = onPlaylistEvent) }
        ),
        TabRowItem(
            title = stringResource(R.string.artists),
            screen = { PlaylistsScreen(state = playlistsState, onEvent = onPlaylistEvent) }
        )
    )

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .height(5.dp),
                color = MaterialTheme.colorScheme.secondary
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        divider = {}
    ) {
        tabRowItems.forEachIndexed { index, item ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            index
                        )
                    }
                },
                text = {
                    Text(
                        text = item.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )
        }
    }
    HorizontalPager(
        count = tabRowItems.size,
        state = pagerState
    ) {
        tabRowItems[it].screen()
    }
}