package com.brokentelephone.game.features.profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.badge.BadgeElement
import com.brokentelephone.game.core.post.DrawPostImage
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberRelativeTime
import com.brokentelephone.game.domain.post.PostContent
import com.brokentelephone.game.domain.post.PostStatus
import com.brokentelephone.game.features.dashboard.model.PostUi

@Composable
fun ProfilePostElement(
    post: PostUi,
    modifier: Modifier = Modifier,
    onMoreClick: () -> Unit = {},
) {
    val relativeTime = rememberRelativeTime(post.createdAt)

    Column(modifier = modifier.fillMaxWidth()) {

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

        Spacer(modifier = Modifier.height(8.dp))

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
                        .clip(RoundedCornerShape(12.dp))
                        .height(200.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            BadgeElement(
                iconResId = R.drawable.ic_mutations,
                text = "${post.generation}/${post.maxGenerations}",
            )

            BadgeElement(
                iconResId = R.drawable.ic_clock,
                text = stringResource(R.string.badge_seconds, post.nextTimeLimit),
            )
        }
    }
}

@Preview
@Composable
private fun ProfilePostElementDrawingPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            ProfilePostElement(
                post = PostUi(
                    id = "1",
                    parentId = "",
                    authorId = "user-1",
                    authorName = "Alex",
                    avatarUrl = null,
                    content = PostContent.Drawing(),
                    createdAt = System.currentTimeMillis() - 300000,
                    generation = 7,
                    maxGenerations = 10,
                    status = PostStatus.AVAILABLE,
                    nextTimeLimit = 60,
                )
            )
        }
    }
}

@Preview
@Composable
private fun ProfilePostElementTextPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            ProfilePostElement(
                post = PostUi(
                    id = "2",
                    parentId = "",
                    authorId = "user-1",
                    authorName = "Alex",
                    avatarUrl = null,
                    content = PostContent.Text("Once upon a time there was a broken telephone that nobody could fix..."),
                    createdAt = System.currentTimeMillis() - 7200000,
                    generation = 10,
                    maxGenerations = 10,
                    status = PostStatus.AVAILABLE,
                    nextTimeLimit = 30,
                )
            )
        }
    }
}
