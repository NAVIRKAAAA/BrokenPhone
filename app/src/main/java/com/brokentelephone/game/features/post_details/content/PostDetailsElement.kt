package com.brokentelephone.game.features.post_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.brokentelephone.game.R
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.badge.BadgeElement
import com.brokentelephone.game.core.badge.StrongBadgeElement
import com.brokentelephone.game.core.badge.StrongBadgeElementType
import com.brokentelephone.game.core.post.DrawPostImage
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberRelativeTime
import com.brokentelephone.game.data.repository.MockPostRepository
import com.brokentelephone.game.domain.post.PostContent
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.dashboard.model.toUi

@Composable
fun PostDetailsElement(
    post: PostUi,
    modifier: Modifier = Modifier,
    isUsersPost: Boolean = false,
) {

    val relativeTime = rememberRelativeTime(post.createdAt)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarComponent(
                avatarUrl = post.avatarUrl,
                size = 40.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = post.authorName,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = relativeTime,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
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

            if(post.isCompleted) {
                StrongBadgeElement(type = StrongBadgeElementType.COMPLETE)
            }

            if(isUsersPost) {
                StrongBadgeElement(type = StrongBadgeElementType.YOU)
            }

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
fun PostDetailsElementPreview() {
    val post = MockPostRepository.mockList[1].toUi()

    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            PostDetailsElement(
                post = post
            )
        }
    }
}