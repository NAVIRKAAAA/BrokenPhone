package com.brokentelephone.game.features.chain_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.shimmer.shimmer
import com.brokentelephone.game.core.ext.modifier.coloredShadow
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.model.post.PostContent

@Composable
fun ChainDetailsElementShimmer(
    content: PostContent,
    modifier: Modifier = Modifier,
) {

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
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = "Username Te",
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.shimmer(cornerRadius = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "2m ago",
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.shimmer(cornerRadius = 4.dp)
                        )
                    }

                }
            }


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
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmer(cornerRadius = 12.dp)
                )
            }
        }

    }
}

@Preview
@Composable
private fun ChainDetailsElementShimmerTextPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            ChainDetailsElementShimmer(content = PostContent.Text(""))
        }
    }
}

@Preview
@Composable
private fun ChainDetailsElementShimmerDrawingPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            ChainDetailsElementShimmer(content = PostContent.Drawing())
        }
    }
}
