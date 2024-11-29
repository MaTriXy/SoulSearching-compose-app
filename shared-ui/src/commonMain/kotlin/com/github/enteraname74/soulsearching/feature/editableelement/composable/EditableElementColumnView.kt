package com.github.enteraname74.soulsearching.feature.editableelement.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import com.github.enteraname74.soulsearching.coreui.UiConstants
import com.github.enteraname74.soulsearching.coreui.textfield.SoulTextFieldHolder
import com.github.enteraname74.soulsearching.coreui.theme.color.SoulSearchingColorTheme
import com.github.enteraname74.soulsearching.feature.editableelement.domain.EditableElement

@Composable
fun EditableElementColumnView(
    coverSectionTitle: String,
    editableElement: EditableElement,
    onSelectImage: () -> Unit,
    focusManager: FocusManager,
    textFields: List<SoulTextFieldHolder>,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(SoulSearchingColorTheme.colorScheme.primary)
            .padding(
                top = UiConstants.Spacing.small,
                start = UiConstants.Spacing.medium,
                end = UiConstants.Spacing.medium,
            )
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.large)
    ) {
        EditableElementCoverSection(
            title = coverSectionTitle,
            editableElement = editableElement,
            onSelectImage = onSelectImage,
        )
        EditableElementTextFieldsView(
            focusManager = focusManager,
            textFields = textFields,
            modifier = Modifier
                .fillMaxWidth(0.9f),
        )
    }
}