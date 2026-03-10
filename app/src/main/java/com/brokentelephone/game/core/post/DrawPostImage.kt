package com.brokentelephone.game.core.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.brokentelephone.game.domain.post.PostContent
import java.io.File

@Composable
fun DrawPostImage(
    content: PostContent.Drawing,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val model = content.localPath?.let { File(it) } ?: content.imageUrl
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(model)
                .crossfade(true)
                .build(),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = contentScale,
            loading = {
//                ShimmerEffect(
//                    modifier = Modifier.fillMaxSize()
//                )
            },
            error = {
//                ShimmerEffect(
//                    modifier = Modifier.fillMaxSize()
//                )
            }
        )
    }
}

@Preview
@Composable
fun DrawPostImagePreview() {
    DrawPostImage(
        content = PostContent.Drawing(),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .height(200.dp),
    )
}
