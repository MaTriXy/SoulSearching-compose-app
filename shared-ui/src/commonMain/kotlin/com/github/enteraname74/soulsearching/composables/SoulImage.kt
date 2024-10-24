package com.github.enteraname74.soulsearching.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.BitmapImage
import coil3.Image
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.github.enteraname74.domain.model.Cover
import com.github.enteraname74.soulsearching.coreui.strings.strings
import com.github.enteraname74.soulsearching.coreui.theme.color.SoulSearchingColorTheme
import com.github.enteraname74.soulsearching.di.injectElement
import com.github.enteraname74.soulsearching.features.filemanager.cover.CoverFileManager
import com.github.enteraname74.soulsearching.features.filemanager.util.CoverUtils
import com.github.enteraname74.soulsearching.shared_ui.generated.resources.Res
import com.github.enteraname74.soulsearching.shared_ui.generated.resources.saxophone_png
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.painterResource
import java.util.*

@Composable
fun SoulImage(
    cover: Cover?,
    size: Dp,
    modifier: Modifier = Modifier,
    roundedPercent: Int = 10,
    tint: Color = SoulSearchingColorTheme.colorScheme.onSecondary,
    contentScale: ContentScale = ContentScale.Crop,
    onSuccess: ((bitmap: ImageBitmap?) -> Unit)? = null,
    builderOptions: ImageRequest.Builder.() -> ImageRequest.Builder = { this },
) {
    val baseModifier = Modifier
        .size(size)
        .clip(RoundedCornerShape(percent = roundedPercent))
        .then(modifier)

    when (cover) {
        null -> {
            TemplateImage(
                modifier = baseModifier,
                contentScale = contentScale,
                tint = tint,
            )
        }

        is Cover.CoverFile -> {
            FileCover(
                cover = cover,
                modifier = baseModifier,
                contentScale = contentScale,
                tint = tint,
                onSuccess = onSuccess,
                builderOptions = builderOptions,
            )
        }
    }
}

@Composable
private fun FileCover(
    cover: Cover.CoverFile,
    modifier: Modifier,
    contentScale: ContentScale,
    tint: Color,
    onSuccess: ((bitmap: ImageBitmap?) -> Unit)? = null,
    builderOptions: ImageRequest.Builder.() -> ImageRequest.Builder = { this },
) {
    when {
        cover.fileCoverId != null -> {
            CoverIdImage(
                coverId = cover.fileCoverId,
                modifier = modifier,
                contentScale = contentScale,
                tint = tint,
                onSuccess = onSuccess,
                builderOptions = builderOptions,
            )
        }

        cover.initialCoverPath != null -> {
            MusicFileImage(
                musicPath = cover.initialCoverPath!!,
                modifier = modifier,
                contentScale = contentScale,
                tint = tint,
                onSuccess = onSuccess,
            )
        }

        else -> {
            TemplateImage(
                modifier = modifier,
                contentScale = contentScale,
                tint = tint,
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun MusicFileImage(
    musicPath: String,
    modifier: Modifier,
    contentScale: ContentScale,
    tint: Color,
    onSuccess: ((bitmap: ImageBitmap?) -> Unit)? = null,
) {
    var fileData: ImageBitmap? by remember { mutableStateOf(null) }
    var job: Job? by remember { mutableStateOf(null) }


    LaunchedEffect(musicPath) {
        if (job?.isActive == true) {
            return@LaunchedEffect
        }

        job = CoroutineScope(Dispatchers.IO).launch {
            fileData = runCatching {
                CoverUtils.getCoverOfMusicFile(musicPath = musicPath)?.decodeToImageBitmap()
            }.getOrNull()
            onSuccess?.let { it(fileData) }
        }
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = fileData != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            if (fileData != null) {
                Image(
                    bitmap = fileData!!,
                    contentDescription = null,
                    modifier = modifier,
                    contentScale = contentScale,
                )
            }
        }

        AnimatedVisibility(
            visible = fileData == null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            TemplateImage(
                modifier = modifier,
                contentScale = contentScale,
                tint = tint,
            )
        }
    }
}

@Composable
fun TemplateImage(
    modifier: Modifier,
    contentScale: ContentScale,
    tint: Color,
) {
    Image(
        modifier = modifier,
        painter = painterResource(Res.drawable.saxophone_png),
        contentDescription = strings.image,
        contentScale = contentScale,
        colorFilter = ColorFilter.tint(tint)
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataImage(
    data: Any?,
    modifier: Modifier,
    contentScale: ContentScale,
    builderOptions: ImageRequest.Builder.() -> ImageRequest.Builder = { this },
    onSuccess: ((bitmap: ImageBitmap?) -> Unit)? = null,
) {
    var previousSavedImage: Image? by remember {
        mutableStateOf(null)
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .builderOptions()
            .data(data)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build(),
        onSuccess = { result ->
            if (result.result.image != previousSavedImage) {
                previousSavedImage = result.result.image
                onSuccess?.let {
                    it((result.result.image as? BitmapImage)?.bitmap?.asImageBitmap())
                }
            }
        },
        onError = {
            onSuccess?.let {
                previousSavedImage = null
                it(null)
            }
        },
        placeholder = painterResource(Res.drawable.saxophone_png),
        contentScale = contentScale,
        error = painterResource(Res.drawable.saxophone_png)
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
private fun CoverIdImage(
    coverId: UUID?,
    modifier: Modifier = Modifier,
    tint: Color = SoulSearchingColorTheme.colorScheme.onSecondary,
    contentScale: ContentScale = ContentScale.Crop,
    onSuccess: ((bitmap: ImageBitmap?) -> Unit)? = null,
    builderOptions: ImageRequest.Builder.() -> ImageRequest.Builder = { this },
) {

    val coverFileManager: CoverFileManager = injectElement()

    var coverPath: String? by rememberSaveable {
        mutableStateOf(null)
    }

    LaunchedEffect(coverId) {
        if (coverId == null) {
            coverPath = null
            return@LaunchedEffect
        }
        coverPath = coverFileManager.getCoverPath(id = coverId)
    }

    if (coverPath != null) {
        DataImage(
            data = coverPath,
            modifier = modifier,
            contentScale = contentScale,
            builderOptions = builderOptions,
            onSuccess = onSuccess,
        )
    } else {
        TemplateImage(
            modifier = modifier,
            contentScale = contentScale,
            tint = tint,
        )
    }
}


