package com.github.soulsearching.ui.theme

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.soulsearching.Constants
import com.github.soulsearching.classes.ColorPaletteUtils
import com.github.soulsearching.classes.PlayerUtils
import com.github.soulsearching.classes.SettingsUtils
import com.github.soulsearching.classes.enumsAndTypes.ColorThemeType

object DynamicColor {
    val primary: Color
        @Composable
        get() = animateColorAsState(
            targetValue =
            if (
                SettingsUtils.settingsViewModel.colorTheme == ColorThemeType.DYNAMIC
                && PlayerUtils.playerViewModel.currentColorPalette != null
            ) {
                ColorPaletteUtils.getDynamicPrimaryColor()
            } else if (SettingsUtils.settingsViewModel.isPersonalizedDynamicPlaylistThemeOn()
                && SettingsUtils.settingsViewModel.playlistPalette != null
            ) {
                ColorPaletteUtils.getDynamicPrimaryColor(
                    SettingsUtils.settingsViewModel.playlistPalette!!.rgb
                )
            } else {
                MaterialTheme.colorScheme.primary
            },
            tween(Constants.AnimationTime.normal)
        ).value

    val onPrimary: Color
        @Composable
        get() = animateColorAsState(
            targetValue =
            if (
                (
                        SettingsUtils.settingsViewModel.colorTheme == ColorThemeType.DYNAMIC
                                && PlayerUtils.playerViewModel.currentColorPalette != null
                        )
                || (
                        SettingsUtils.settingsViewModel.isPersonalizedDynamicPlaylistThemeOn()
                                && SettingsUtils.settingsViewModel.playlistPalette != null
                        )
            ) {
                Color.White
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            tween(Constants.AnimationTime.normal)
        ).value


    val secondary: Color
        @Composable
        get() = animateColorAsState(
            targetValue =
            if (SettingsUtils.settingsViewModel.colorTheme == ColorThemeType.DYNAMIC
                && PlayerUtils.playerViewModel.currentColorPalette != null
            ) {
                ColorPaletteUtils.getDynamicSecondaryColor()
            } else if (SettingsUtils.settingsViewModel.isPersonalizedDynamicPlaylistThemeOn()
                && SettingsUtils.settingsViewModel.playlistPalette != null
            ) {
                ColorPaletteUtils.getDynamicSecondaryColor(
                    SettingsUtils.settingsViewModel.playlistPalette!!.rgb
                )
            } else {
                MaterialTheme.colorScheme.secondary
            },
            tween(Constants.AnimationTime.normal)
        ).value

    val onSecondary: Color
        @Composable
        get() = animateColorAsState(
            targetValue =
            if (
                (
                        SettingsUtils.settingsViewModel.colorTheme == ColorThemeType.DYNAMIC
                                && PlayerUtils.playerViewModel.currentColorPalette != null
                        )
                || (
                        SettingsUtils.settingsViewModel.isPersonalizedDynamicPlaylistThemeOn()
                                && SettingsUtils.settingsViewModel.playlistPalette != null
                        )
            ) {
                Color.White
            } else {
                MaterialTheme.colorScheme.onSecondary
            },
            tween(Constants.AnimationTime.normal)
        ).value

    val subText: Color
        @Composable
        get() = animateColorAsState(
            targetValue =
            if (
                (
                        SettingsUtils.settingsViewModel.colorTheme == ColorThemeType.DYNAMIC
                                && PlayerUtils.playerViewModel.currentColorPalette != null
                        )
                || (
                        SettingsUtils.settingsViewModel.isPersonalizedDynamicPlaylistThemeOn()
                                && SettingsUtils.settingsViewModel.playlistPalette != null
                        )
            ) {
                Color.LightGray
            } else {
                MaterialTheme.colorScheme.outline
            },
            tween(Constants.AnimationTime.normal)
        ).value
}