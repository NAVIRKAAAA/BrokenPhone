package com.broken.telephone.features.chain_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.avatar.AvatarComponent
import com.broken.telephone.core.modifier.hidden
import com.broken.telephone.core.post.DrawPostImage
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.core.utils.rememberRelativeTime
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.features.dashboard.model.PostUi

@Composable
fun ChainDetailsElement(
    post: PostUi,
    modifier: Modifier = Modifier,
    isHidden: Boolean = false
) {
    val relativeTime = rememberRelativeTime(post.createdAt)

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

        AvatarComponent(
            avatarUrl = post.avatarUrl,
            size = 40.dp,
            modifier = if (isHidden) Modifier.hidden(cornerRadius = 50.dp) else Modifier
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
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .then(if (isHidden) Modifier.hidden(cornerRadius = 4.dp) else Modifier)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = relativeTime,
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = if (isHidden) Modifier.hidden(cornerRadius = 4.dp) else Modifier
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            when (val content = post.content) {
                is PostContent.Text -> {
                    Text(
                        text = content.text,
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        modifier = if (isHidden) Modifier.hidden(cornerRadius = 4.dp) else Modifier,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                is PostContent.Drawing -> {
                    DrawPostImage(
                        content = content,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .height(200.dp)
                            .then(if (isHidden) Modifier.hidden(cornerRadius = 12.dp) else Modifier),
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun ChainDetailsElementPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            ChainDetailsElement(
                post = PostUi(
                    id = "1",
                    parentId = "",
                    authorId = "user-1",
                    authorName = "Alex",
                    avatarUrl = null,
//                    content = PostContent.Drawing(),
            content = PostContent.Text("Once upon a time there was a broken telephone..."),
                    createdAt = System.currentTimeMillis() - 300000,
                    generation = 10,
                    maxGenerations = 10,
                    status = PostStatus.AVAILABLE,
                    nextTimeLimit = 60,
                ),
                isHidden = false
            )
        }
    }
}