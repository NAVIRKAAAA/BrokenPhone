package com.broken.telephone.features.post_details.content

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import java.io.File
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.badge.BadgeElement
import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.features.dashboard.model.PostUi
import com.broken.telephone.features.dashboard.model.toUi

@Composable
fun PostDetailsElement(
    post: PostUi,
    modifier: Modifier = Modifier
) {

    val relativeTime = remember(post.createdAt) {
        DateUtils.getRelativeTimeSpanString(
            post.createdAt,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_ALL
        ).toString()
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = post.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(12.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            BadgeElement(
                iconResId = R.drawable.ic_mutations,
                text = "${post.generation}/${post.maxGenerations}",
                onClick = {},
                enabled = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            BadgeElement(
                iconResId = R.drawable.ic_clock,
                text = "${post.content.timeLimit}s",
                onClick = {},
                enabled = false
            )

        }
    }

}

@Preview
@Composable
fun PostDetailsElementPreview() {
    val post = MockPostRepository.mockList.first().toUi()
    PostDetailsElement(
        post = post
    )
}