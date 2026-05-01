package com.brokentelephone.game.features.add_friend.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.profile.FriendItemShimmer
import com.brokentelephone.game.core.composable.profile.FriendRequestItemShimmer
import com.brokentelephone.game.core.composable.shimmer.shimmer
import com.brokentelephone.game.core.composable.text_field.SearchTextFieldHeight
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddFriendShimmerList(
    modifier: Modifier = Modifier,
) {
    val searchBarTopOffset = SearchTextFieldHeight + 24.dp

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = searchBarTopOffset, bottom = 16.dp),
        userScrollEnabled = false,
    ) {
        stickyHeader {
            ShimmerHeader(title = stringResource(R.string.add_friend_received_invites))
        }

        items(2) { index ->
            Column {
                if (index != 0) Spacer(modifier = Modifier.height(16.dp))
                FriendRequestItemShimmer(modifier = Modifier.padding(horizontal = 16.dp))
                if (index != 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.appColors.divider)
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        stickyHeader {
            ShimmerHeader(title = stringResource(R.string.add_friend_pending_invites))
        }

        items(3) { index ->
            Column {
                if (index != 0) Spacer(modifier = Modifier.height(16.dp))
                FriendItemShimmer(modifier = Modifier.padding(horizontal = 16.dp))
                if (index != 2) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.appColors.divider)
                }
            }
        }
    }
}

@Composable
private fun ShimmerHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "00",
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.shimmer(cornerRadius = 4.dp),
        )
    }
}

@Preview
@Composable
private fun AddFriendShimmerListPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        AddFriendShimmerList(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
