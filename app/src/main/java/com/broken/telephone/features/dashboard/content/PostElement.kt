package com.broken.telephone.features.dashboard.content

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.badge.BadgeElement
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.features.create_post.model.CreatePostState
import com.broken.telephone.features.dashboard.model.PostUi

@Composable
fun PostElement(
    post: PostUi,
    modifier: Modifier = Modifier,
) {

    val relativeTime = remember(post.createdAt) {
        DateUtils.getRelativeTimeSpanString(
            post.createdAt,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_ALL
        ).toString()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
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

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_horizontal_menu),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false),
                        onClick = {}
                    )
                )

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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .height(200.dp)
                            .background(Color.LightGray)
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
                    text = when (post.content) {
                        is PostContent.Text -> "${post.textTimeLimit}s"
                        is PostContent.Drawing -> "${post.drawingTimeLimit}s"
                    },
                    onClick = {},
                    enabled = false
                )

            }
        }
    }
}

@Preview
@Composable
fun PostElementPreview() {
    PostElement(
        post = PostUi(
            id = "1",
            authorName = "Alex".repeat(55),
            avatarUrl = null,
            content = PostContent.Text("Once upon a time there was a broken telephone..."),
            createdAt = System.currentTimeMillis() - 300000,
            generation = 1,
            maxGenerations = 10,
            textTimeLimit = 30,
            drawingTimeLimit = 60,
        ),
    )
}
