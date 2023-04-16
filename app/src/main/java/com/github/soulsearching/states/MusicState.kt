package com.github.soulsearching.states

import android.graphics.Bitmap
import com.github.soulsearching.database.model.Music

data class MusicState(
    val musics: List<Music> = emptyList(),
    val isBottomSheetShown : Boolean = false,
    val isDeleteDialogShown : Boolean = false,
    val isAddToPlaylistDialogShown : Boolean = false,
    val selectedMusic : Music = Music(),
    var name : String = "",
    var artist : String = "",
    var album : String = "",
    var cover : Bitmap? = null
)