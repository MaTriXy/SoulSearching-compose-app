package com.github.soulsearching.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.soulsearching.Constants
import com.github.soulsearching.R
import com.github.soulsearching.SoulSearchingContext
import com.github.soulsearching.classes.utils.AndroidUtils
import com.github.soulsearching.composables.AppHeaderBar
import com.github.soulsearching.composables.AppImage
import com.github.soulsearching.composables.AppTextField
import com.github.soulsearching.events.AlbumEvent
import com.github.soulsearching.states.SelectedAlbumState
import com.github.soulsearching.theme.SoulSearchingColorTheme
import com.github.soulsearching.types.ScreenOrientation
import com.github.soulsearching.viewmodel.ModifyAlbumViewModel
import java.util.UUID

@Composable
fun ModifyAlbumScreen(
    modifyAlbumViewModel: ModifyAlbumViewModel,
    selectedAlbumId: String,
    finishAction: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val resultImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                modifyAlbumViewModel.handler.onAlbumEvent(
                    AlbumEvent.SetCover(
                        AndroidUtils.getBitmapFromUri(uri as Uri, context.contentResolver)
                    )
                )
            }
        }

    fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultImageLauncher.launch(intent)
    }

    var isAlbumFetched by rememberSaveable {
        mutableStateOf(false)
    }

    if (!isAlbumFetched) {
        modifyAlbumViewModel.handler.onAlbumEvent(
            AlbumEvent.AlbumFromID(
                albumId = UUID.fromString(selectedAlbumId)
            )
        )
        isAlbumFetched = true
    }

    val albumState by modifyAlbumViewModel.handler.state.collectAsState()

    Scaffold(
        topBar = {
            AppHeaderBar(
                title = stringResource(id = R.string.album_information),
                leftAction = finishAction,
                rightIcon = Icons.Rounded.Done,
                rightAction = {
                    modifyAlbumViewModel.handler.onAlbumEvent(AlbumEvent.UpdateAlbum)
                    finishAction()
                }
            )
        },
        content = { padding ->
            when (SoulSearchingContext.orientation) {
                ScreenOrientation.HORIZONTAL -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SoulSearchingColorTheme.colorScheme.secondary)
                            .padding(padding)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    focusManager.clearFocus()
                                })
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(Constants.Spacing.medium)
                                .weight(1F),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = Constants.Spacing.medium),
                                text = stringResource(id = R.string.album_cover),
                                color = SoulSearchingColorTheme.colorScheme.onSecondary
                            )
                            AppImage(
                                bitmap = albumState.albumCover,
                                size = 200.dp,
                                modifier = Modifier.clickable { selectImage() }
                            )
                        }
                        ModifyAlbumTextFields(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2F)
                                .background(color = SoulSearchingColorTheme.colorScheme.primary)
                                .padding(Constants.Spacing.medium),
                            selectedAlbumState = albumState,
                            focusManager = focusManager,
                            setName = { modifyAlbumViewModel.handler.onAlbumEvent(AlbumEvent.SetName(it)) },
                            setArtist = {
                                modifyAlbumViewModel.handler.onAlbumEvent(
                                    AlbumEvent.SetArtist(
                                        it
                                    )
                                )
                            }
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SoulSearchingColorTheme.colorScheme.secondary)
                            .padding(padding)
                            .padding(top = Constants.Spacing.medium)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    focusManager.clearFocus()
                                })
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(Constants.Spacing.medium),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = Constants.Spacing.medium),
                                text = stringResource(id = R.string.album_cover),
                                color = SoulSearchingColorTheme.colorScheme.onSecondary
                            )
                            AppImage(
                                bitmap = albumState.albumCover,
                                size = 200.dp,
                                modifier = Modifier.clickable { selectImage() }
                            )
                        }
                        ModifyAlbumTextFields(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1F)
                                .clip(RoundedCornerShape(topStart = 50f, topEnd = 50f))
                                .background(color = SoulSearchingColorTheme.colorScheme.primary)
                                .padding(Constants.Spacing.medium),
                            selectedAlbumState = albumState,
                            focusManager = focusManager,
                            setName = { modifyAlbumViewModel.handler.onAlbumEvent(AlbumEvent.SetName(it)) },
                            setArtist = {
                                modifyAlbumViewModel.handler.onAlbumEvent(
                                    AlbumEvent.SetArtist(
                                        it
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ModifyAlbumTextFields(
    modifier: Modifier,
    selectedAlbumState: SelectedAlbumState,
    focusManager: FocusManager,
    setName: (String) -> Unit,
    setArtist: (String) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1F)
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(4F),
            verticalArrangement = Arrangement.spacedBy(Constants.Spacing.medium)
        ) {
            AppTextField(
                value = selectedAlbumState.albumWithMusics.album.albumName,
                onValueChange = { setName(it) },
                labelName = stringResource(id = R.string.album_name),
                focusManager = focusManager
            )
            AppTextField(
                value = selectedAlbumState.albumWithMusics.artist!!.artistName,
                onValueChange = { setArtist(it) },
                labelName = stringResource(id = R.string.artist_name),
                focusManager = focusManager
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1F)
        )
    }
}