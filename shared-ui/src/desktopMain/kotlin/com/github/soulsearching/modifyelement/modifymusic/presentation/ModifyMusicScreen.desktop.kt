package com.github.soulsearching.modifyelement.modifymusic.presentation

import androidx.compose.runtime.Composable
import com.github.soulsearching.modifyelement.modifymusic.presentation.composable.ModifyMusicComposable
import com.github.soulsearching.domain.viewmodel.ModifyMusicViewModel
import com.github.soulsearching.modifyelement.modifymusic.domain.ModifyMusicEvent

@Composable
actual fun ModifyMusicScreenView(
    modifyMusicViewModel: ModifyMusicViewModel,
    selectedMusicId: String,
    finishAction: () -> Unit
) {
    ModifyMusicComposable(
        modifyMusicViewModel = modifyMusicViewModel,
        selectedMusicId = selectedMusicId,
        finishAction = {
            modifyMusicViewModel.handler.onEvent(ModifyMusicEvent.UpdateMusic)
            finishAction()
        },
        selectImage = {}
    )
}