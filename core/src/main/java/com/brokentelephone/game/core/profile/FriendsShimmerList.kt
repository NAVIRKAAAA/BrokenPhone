package com.brokentelephone.game.core.profile

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.text_field.SearchTextFieldHeight
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

private val shimmerItems = List(2) { it }

@Composable
fun FriendsShimmerList(
    modifier: Modifier = Modifier,
) {
    val items = remember { shimmerItems }

    val searchBarTopOffset = SearchTextFieldHeight + 24.dp

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = searchBarTopOffset, bottom = 16.dp),
        userScrollEnabled = true,
    ) {
        itemsIndexed(items) { index, _ ->
            Column {
                if (index != 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                FriendItemShimmer(
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                if (index != items.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.appColors.divider)
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        stickyHeader {
            ShimmerHeader(
                title = stringResource(R.string.friends_suggested)
            )
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
    }
}

@Preview
@Composable
private fun FriendsShimmerListPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        FriendsShimmerList(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
