package com.brokentelephone.game.features.post_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.badge.BadgeElement
import com.brokentelephone.game.core.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.post.PostContent

@Composable
fun PostDetailsElementShimmer(
    content: PostContent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer(cornerRadius = 20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Author name",
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.shimmer(cornerRadius = 4.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "2m ago",
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.shimmer(cornerRadius = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (content) {
            is PostContent.Text -> {
                Text(
                    text = "Once upon a time there was a broken telephone and nobody remembered what the original message was.",
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.shimmer(cornerRadius = 4.dp)
                )
            }
            is PostContent.Drawing -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmer(cornerRadius = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgeElement(
                iconResId = R.drawable.ic_mutations,
                text = "7/10",
                modifier = Modifier.shimmer(cornerRadius = 4.dp)
            )

            BadgeElement(
                iconResId = R.drawable.ic_clock,
                text = "60s",
                modifier = Modifier.shimmer(cornerRadius = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun PostDetailsElementShimmerTextPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            PostDetailsElementShimmer(content = PostContent.Text(""))
        }
    }
}

@Preview
@Composable
fun PostDetailsElementShimmerDrawingPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            PostDetailsElementShimmer(content = PostContent.Drawing())
        }
    }
}
