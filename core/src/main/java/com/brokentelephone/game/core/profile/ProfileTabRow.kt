package com.brokentelephone.game.core.profile

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

@Composable
fun ProfileTabRow(
    tabs: List<ProfileTab>,
    selectedIndex: Int,
    onTabSelect: (ProfileTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
        divider = {
            HorizontalDivider(color = MaterialTheme.appColors.divider)
        },
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedIndex == index
            Tab(
                selected = isSelected,
                onClick = { onTabSelect(tab) },
                text = {
                    Text(
                        text = stringResource(tab.labelResId),
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        fontSize = 17.sp,
                        lineHeight = 25.sp,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun ProfileTabRowPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        ProfileTabRow(
            tabs = ProfileTab.entries,
            selectedIndex = 0,
            onTabSelect = {},
        )
    }
}
