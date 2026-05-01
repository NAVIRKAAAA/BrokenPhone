package com.brokentelephone.game.core.composable.profile

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.chip.PostChip
import com.brokentelephone.game.core.composable.post.DrawPostImage
import com.brokentelephone.game.core.ext.modifier.coloredShadow
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberRelativeTime
import com.brokentelephone.game.core.utils.toShortFormattedTime
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus

@Composable
fun UserProfileContributionElement(
    post: PostUi,
    modifier: Modifier = Modifier,
    onMoreClick: () -> Unit = {},
) {
    val relativeTime = rememberRelativeTime(post.createdAt)
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = modifier
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
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = relativeTime,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = painterResource(R.drawable.ic_horizontal_menu),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false),
                    onClick = onMoreClick
                )
            )
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

            PostChip(
                text = "${post.generation}/${post.maxGenerations}",
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconResId = R.drawable.ic_mutations,
            )

            val timeIconResId = when(post.content) {
                is PostContent.Drawing -> R.drawable.ic_brush_v2
                is PostContent.Text -> R.drawable.ic_edit_v2
            }

            PostChip(
                text = post.timeLimit.toShortFormattedTime(),
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconResId = timeIconResId,
                modifier = Modifier,
                enabled = false,
            )
        }
    }
}

@Preview
@Composable
private fun UserProfileContributionElementDrawingPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            UserProfileContributionElement(
                post = PostUi(
                    id = "1",
                    authorId = "user-1",
                    authorName = "Alex",
                    avatarUrl = null,
                    content = PostContent.Drawing(),
                    createdAt = System.currentTimeMillis() - 300000,
                    generation = 7,
                    maxGenerations = 10,
                    status = PostStatus.AVAILABLE,
                    drawingTimeLimit = 60,
                    textTimeLimit = 60,
                    chainSize = 10
                )
            )
        }
    }
}

@Preview
@Composable
private fun UserProfileContributionElementTextPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            UserProfileContributionElement(
                post = PostUi(
                    id = "2",
                    authorId = "user-1",
                    authorName = "Alex",
                    avatarUrl = null,
                    content = PostContent.Text("Once upon a time there was a broken telephone that nobody could fix..."),
                    createdAt = System.currentTimeMillis() - 7200000,
                    generation = 10,
                    maxGenerations = 10,
                    status = PostStatus.AVAILABLE,
                    drawingTimeLimit = 60,
                    textTimeLimit = 60,
                    chainSize = 10
                )
            )
        }
    }
}
