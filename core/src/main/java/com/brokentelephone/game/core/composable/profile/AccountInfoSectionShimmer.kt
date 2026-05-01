package com.brokentelephone.game.core.composable.profile

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun AccountInfoSectionShimmer(
    modifier: Modifier = Modifier,
) {
    val avatarSize = 82.dp
    Column() {
        Row(modifier = modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .shimmer(cornerRadius = 32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .height(avatarSize),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Username pl",
                    fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                    fontSize = 19.sp,
                    lineHeight = 19.sp,
                    modifier = Modifier.shimmer(cornerRadius = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    repeat(3) {
                        Column {
                            Text(
                                text = "00",
                                fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                                fontSize = 17.sp,
                                lineHeight = 17.sp,
                                modifier = Modifier.shimmer(cornerRadius = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "LabelLabe",
                                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                modifier = Modifier.shimmer(cornerRadius = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Member since Jan 2026",
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 14.sp,
            lineHeight = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(vertical = 12.dp).shimmer(cornerRadius = 4.dp),
        )
    }
}

@Preview
@Composable
private fun AccountInfoSectionShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            AccountInfoSectionShimmer()
        }
    }
}
