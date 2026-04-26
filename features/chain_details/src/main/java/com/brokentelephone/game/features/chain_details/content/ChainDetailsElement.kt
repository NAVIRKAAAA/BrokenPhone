package com.brokentelephone.game.features.chain_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.modifier.coloredShadow
import com.brokentelephone.game.core.post.DrawPostImage
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberRelativeTime
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus

@Composable
fun ChainDetailsElement(
    post: PostUi,
    modifier: Modifier = Modifier,
    onUserClick: () -> Unit = {},
) {
    val relativeTime = rememberRelativeTime(post.createdAt)
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .coloredShadow(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                blurRadius = 16f,
                offsetY = 0.dp,
                offsetX = 0.dp,
                shape = shape,
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {

            AvatarComponent(
                avatarUrl = post.avatarUrl,
                size = 40.dp,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onUserClick
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = post.authorName,
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onUserClick
                                )
                        )

                        Text(
                            text = relativeTime,
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }

                }
            }


        }

        Spacer(modifier = Modifier.height(12.dp))

        when (val content = post.content) {
            is PostContent.Text -> {
                Text(
                    text = content.text,
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            is PostContent.Drawing -> {
                DrawPostImage(
                    content = content,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }

    }
}

@Preview
@Composable
fun ChainDetailsElementPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
        ) {
            ChainDetailsElement(
                post = PostUi(
                    id = "1",
                    authorId = "user-1",
                    authorName = "Alex",
                    avatarUrl = null,
//                    content = PostContent.Drawing(),
                    content = PostContent.Text("Once upon a time there was a broken telephone..."),
                    createdAt = System.currentTimeMillis() - 300000,
                    generation = 10,
                    maxGenerations = 10,
                    status = PostStatus.AVAILABLE,
                    drawingTimeLimit = 60,
                    textTimeLimit = 60
                ),
            )
        }
    }
}