package com.broken.telephone.features.profile.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme

@Composable
fun ProfileTopBar(
    title: String,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    showEditButton: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.primary,
        )

        Row {
            if (showEditButton) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.profile_top_bar_edit),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
            }

            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.profile_top_bar_settings),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileTopBarPreview() {
    BrokenTelephoneTheme {
        ProfileTopBar(
            title = "Profile",
            onEditClick = {},
            onSettingsClick = {},
        )
    }
}
