package com.broken.telephone.features.chain_details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.broken.telephone.R
import com.broken.telephone.core.avatar.AvatarComponent
import com.broken.telephone.core.utils.rememberRelativeTime
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.features.dashboard.model.PostUi
import java.io.File

@Composable
fun ChainDetailsElement(
    post: PostUi,
    modifier: Modifier = Modifier
) {


    val relativeTime = rememberRelativeTime(post.createdAt)

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

        AvatarComponent(
            avatarUrl = post.avatarUrl,
            size = 40.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = post.authorName,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = relativeTime,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.Gray,
                        maxLines = 1,
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            when (val content = post.content) {
                is PostContent.Text -> {
                    Text(
                        text = content.text,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                    )
                }

                is PostContent.Drawing -> {
                    val model = content.localPath?.let { File(it) } ?: content.imageUrl

                    AsyncImage(
                        model = model,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .height(200.dp)
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun ChainDetailsElementPreview() {
    ChainDetailsElement(
        post = PostUi(
            id = "1",
            parentId = "",
            authorId = "user-1",
            authorName = "Alex".repeat(55),
            avatarUrl = null,
            content = PostContent.Text("Once upon a time there was a broken telephone..."),
            createdAt = System.currentTimeMillis() - 300000,
            generation = 10,
            maxGenerations = 10,
            status = PostStatus.AVAILABLE,
            nextTimeLimit = 60,
        ),
    )
}