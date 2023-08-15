package com.github.soulsearching.states

import android.graphics.Bitmap
import com.github.soulsearching.classes.SortDirection
import com.github.soulsearching.classes.SortType
import com.github.soulsearching.database.model.ArtistWithMusics

data class ArtistState(
    val artists: List<ArtistWithMusics> = emptyList(),
    val selectedArtist : ArtistWithMusics = ArtistWithMusics(),
    val cover : Bitmap? = null,
    val name : String = "",
    val isDeleteDialogShown: Boolean = false,
    val isBottomSheetShown: Boolean = false,
    var sortType: Int = SortType.NAME,
    var sortDirection: Int = SortDirection.ASC
)