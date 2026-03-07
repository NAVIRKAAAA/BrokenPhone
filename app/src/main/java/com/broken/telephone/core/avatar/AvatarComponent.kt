package com.broken.telephone.core.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.broken.telephone.core.shimmer.ShimmerEffect

@Composable
fun AvatarComponent(
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(avatarUrl)
                .crossfade(true)
                .build(),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = {
                ShimmerEffect(
                    modifier = Modifier.fillMaxSize()
                )
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        )
    }

}

@Preview
@Composable
fun AvatarComponentPreview() {
    AvatarComponent(
        avatarUrl = "",
        modifier = Modifier
    )
}