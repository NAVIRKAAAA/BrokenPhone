package com.broken.telephone.features.dashboard.content

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.badge.BadgeElement
import com.broken.telephone.core.shimmer.shimmer
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.domain.post.PostContent

@Composable
fun PostElementShimmer(
    content: PostContent,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmer(cornerRadius = 20.dp)
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

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_horizontal_menu),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.shimmer(cornerRadius = 4.dp)
                )
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

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

            Spacer(modifier = Modifier.padding(top = 16.dp))

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
}

@Preview
@Composable
fun PostElementShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            PostElementShimmer(content = PostContent.Text(""))
        }
    }
}
