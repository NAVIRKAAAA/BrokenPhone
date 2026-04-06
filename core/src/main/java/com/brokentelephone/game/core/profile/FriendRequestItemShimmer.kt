package com.brokentelephone.game.core.profile

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
import com.brokentelephone.game.core.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun FriendRequestItemShimmer(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer(),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Username",
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.shimmer(cornerRadius = 4.dp),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Member since Jan 2024",
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    modifier = Modifier.shimmer(cornerRadius = 4.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 52.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .shimmer(cornerRadius = 12.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .shimmer(cornerRadius = 12.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendRequestItemShimmerDarkPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendRequestItemShimmer()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendRequestItemShimmerLightPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendRequestItemShimmer()
        }
    }
}
