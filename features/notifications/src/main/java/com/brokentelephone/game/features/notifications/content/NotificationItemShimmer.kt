package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
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
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun FriendNotificationItemShimmer(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(modifier = Modifier
            .padding(top = 20.dp)
            .size(8.dp))

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .shimmer(cornerRadius = 22.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Username sent you you you",
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 15.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier
                        .shimmer(cornerRadius = 4.dp),
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "5 хв",
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.shimmer(cornerRadius = 4.dp),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .shimmer(cornerRadius = 14.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .shimmer(cornerRadius = 14.dp),
                )
            }
        }
    }
}

@Composable
fun ChainNotificationItemShimmer(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.size(8.dp))

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmer(cornerRadius = 12.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Your chain is com",
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 15.sp,
                lineHeight = 15.sp,
                modifier = Modifier.shimmer(cornerRadius = 4.dp),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Someone finished the chain chain",
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 13.sp,
                lineHeight = 13.sp,
                modifier = Modifier.shimmer(cornerRadius = 4.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "5 хв",
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            modifier = Modifier.shimmer(cornerRadius = 4.dp),
        )
    }
}

@Composable
fun NewsNotificationItemShimmer(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.size(8.dp))

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmer(cornerRadius = 12.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "New feature dropped",
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 15.sp,
                lineHeight = 15.sp,
                modifier = Modifier.shimmer(cornerRadius = 4.dp),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "You can now use 3 new brush types",
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                modifier = Modifier.shimmer(cornerRadius = 4.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "1 год",
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            modifier = Modifier.shimmer(cornerRadius = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendNotificationItemShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendNotificationItemShimmer()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChainNotificationItemShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            ChainNotificationItemShimmer()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsNotificationItemShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NewsNotificationItemShimmer()
        }
    }
}
