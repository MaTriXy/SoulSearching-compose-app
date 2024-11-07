package com.github.enteraname74.soulsearching.feature.mainpage.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.enteraname74.domain.model.SortDirection
import com.github.enteraname74.domain.model.SortType
import com.github.enteraname74.soulsearching.coreui.SoulPlayerSpacer
import com.github.enteraname74.soulsearching.coreui.UiConstants

@Composable
fun <T> MainPageList(
    list: List<T>,
    title: String,
    rightComposable: @Composable () -> Unit = {},
    innerComposable: @Composable (() -> Unit)? = null,
    setSortType: (Int) -> Unit = {},
    toggleSortDirection: () -> Unit = {},
    sortType: Int = SortType.NAME,
    sortDirection: Int = SortDirection.DESC,
    isUsingSort: Boolean = true,
    key: ((T) -> Any)?,
    contentType: (T) -> Any,
    item: @Composable LazyGridItemScope.(element: T) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.mediumPlus)
    ) {
        SubMenuComposable(
            title = title,
            setSortType = setSortType,
            toggleSortDirection = toggleSortDirection,
            sortType = sortType,
            sortDirection = sortDirection,
            rightComposable = rightComposable,
            isUsingSort = isUsingSort
        )
        innerComposable?.let { it() }
        if (list.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(UiConstants.ImageSize.veryLarge),
                contentPadding = PaddingValues(
                    horizontal = UiConstants.Spacing.medium,
                ),
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(UiConstants.Spacing.medium),
            ) {
                items(
                    items = list,
                    key = key,
                    contentType = contentType,
                ) { element ->
                    item(element)
                }

                item {
                    SoulPlayerSpacer()
                }
            }
        } else {
            NoElementView()
        }
    }
}