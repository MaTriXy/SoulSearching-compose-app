package com.github.soulsearching.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.github.soulsearching.Constants
import com.github.soulsearching.R
import com.github.soulsearching.composables.AppHeaderBar
import com.github.soulsearching.composables.PlayerSpacer
import com.github.soulsearching.composables.settings.SettingsElement
import com.github.soulsearching.composables.settings.SettingsSwitchElement
import com.github.soulsearching.di.injectElement
import com.github.soulsearching.model.settings.ViewSettingsManager
import com.github.soulsearching.theme.SoulSearchingColorTheme

@Composable
fun SettingsPersonalisationScreen(
    finishAction: () -> Unit,
    viewSettingsManager: ViewSettingsManager = injectElement()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoulSearchingColorTheme.colorScheme.primary)
    ) {
        AppHeaderBar(
            title = stringResource(id = R.string.personalization_title),
            leftAction = finishAction
        )
        LazyColumn {
            item { 
                SettingsElement(
                    title = stringResource(id = R.string.main_page_title),
                    text = stringResource(id = R.string.main_page_text),
                    padding = PaddingValues(Constants.Spacing.large)
                )
            }
            item {
                SettingsSwitchElement(
                    title = stringResource(id = R.string.show_quick_access),
                    toggleAction = { viewSettingsManager.toggleQuickAccessVisibility() },
                    isChecked = viewSettingsManager.isQuickAccessShown,
                    titleFontSize = 16.sp,
                    padding = PaddingValues(
                        start = Constants.Spacing.veryLarge,
                        end = Constants.Spacing.veryLarge,
                        top = Constants.Spacing.medium,
                        bottom = Constants.Spacing.medium
                    )
                )
            }
            item {
                SettingsSwitchElement(
                    title = stringResource(id = R.string.show_playlists),
                    toggleAction = { viewSettingsManager.togglePlaylistsVisibility() },
                    isChecked = viewSettingsManager.isPlaylistsShown,
                    titleFontSize = 16.sp,
                    padding = PaddingValues(
                        start = Constants.Spacing.veryLarge,
                        end = Constants.Spacing.veryLarge,
                        top = Constants.Spacing.medium,
                        bottom = Constants.Spacing.medium
                    )
                )
            }
            item {
                SettingsSwitchElement(
                    title = stringResource(id = R.string.show_albums),
                    toggleAction = { viewSettingsManager.toggleAlbumsVisibility() },
                    isChecked = viewSettingsManager.isAlbumsShown,
                    titleFontSize = 16.sp,
                    padding = PaddingValues(
                        start = Constants.Spacing.veryLarge,
                        end = Constants.Spacing.veryLarge,
                        top = Constants.Spacing.medium,
                        bottom = Constants.Spacing.medium
                    )
                )
            }
            item {
                SettingsSwitchElement(
                    title = stringResource(id = R.string.show_artists),
                    toggleAction = { viewSettingsManager.toggleArtistsVisibility() },
                    isChecked = viewSettingsManager.isArtistsShown,
                    titleFontSize = 16.sp,
                    padding = PaddingValues(
                        start = Constants.Spacing.veryLarge,
                        end = Constants.Spacing.veryLarge,
                        top = Constants.Spacing.medium,
                        bottom = Constants.Spacing.medium
                    )
                )
            }
            item {
                SettingsSwitchElement(
                    title = stringResource(id = R.string.show_vertical_access_bar_title),
                    text = stringResource(id = R.string.show_vertical_access_bar_text),
                    toggleAction = { viewSettingsManager.toggleVerticalBarVisibility() },
                    isChecked = viewSettingsManager.isVerticalBarShown,
                    titleFontSize = 16.sp,
                    padding = PaddingValues(
                        start = Constants.Spacing.veryLarge,
                        end = Constants.Spacing.veryLarge,
                        top = Constants.Spacing.medium,
                        bottom = Constants.Spacing.medium
                    )
                )
            }
            item {
                PlayerSpacer()
            }
        }
    }
}