package com.brokentelephone.game.features.post_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.chip.PostChip
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.post.DrawPostImage
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.core.utils.rememberRelativeTime
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus

@Composable
fun PostDetailsElement(
    post: PostUi,
    modifier: Modifier = Modifier,
    onUserClick: () -> Unit = {},
) {

    val relativeTime = rememberRelativeTime(post.createdAt)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
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

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = post.authorName,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = 16.dp)
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

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {

            if (post.isCompleted) {
                PostChip(
                    text = stringResource(R.string.dashboard_badge_complete),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.appColors.badgeCompleteContainer,
                    iconResId = R.drawable.ic_check,
                )
            } else {

                PostChip(
                    text = "${post.generation}/${post.maxGenerations}",
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    iconResId = R.drawable.ic_mutations,
                )

                PostChip(
                    text = stringResource(R.string.badge_seconds, post.nextTimeLimit),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    iconResId = R.drawable.ic_clock,
                )
            }
        }
    }

}

@Preview
@Composable
fun PostDetailsElementPreview() {
//    val post = MockPostRepository.mockList[1].toUi()

    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            PostDetailsElement(
                post = PostUi(
                    id = "2",
                    authorId = "user-1",
                    authorName = "Alex",
                    avatarUrl = null,
                    content = PostContent.Text("Once upon a time there was a broken telephone that nobody could fix..."),
                    createdAt = System.currentTimeMillis() - 7200000,
                    generation = 7,
                    maxGenerations = 10,
                    status = PostStatus.AVAILABLE,
                    drawingTimeLimit = 60,
                    textTimeLimit = 60
                )
            )
        }
    }
}