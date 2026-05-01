package com.brokentelephone.game.features.user_friends.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.shimmer.ShimmerContent
import com.brokentelephone.game.core.composable.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun UserFriendsTopBar(
    username: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        ShimmerContent(
            isLoading = username == null,
            shimmerContent = {
                Text(
                    text = "username_123",
                    fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                    fontSize = 17.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.shimmer(cornerRadius = 4.dp),
                )
            },
            content = {
                Text(
                    text = username ?: "",
                    fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserFriendsTopBarPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            UserFriendsTopBar(
                username = "alex_username",
                onBackClick = {},
            )
        }
    }
}
