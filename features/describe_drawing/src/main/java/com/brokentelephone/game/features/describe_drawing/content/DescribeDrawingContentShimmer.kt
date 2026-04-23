package com.brokentelephone.game.features.describe_drawing.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.brokentelephone.game.core.chip.PostChip
import com.brokentelephone.game.core.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

@Composable
fun DescribeDrawingContentShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp))
                .shimmer(cornerRadius = 14.dp)
        )

        Spacer(modifier = Modifier.weight(1f).height(16.dp))

        HorizontalDivider(color = MaterialTheme.appColors.divider)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Once upon a time there was a broken telephone\nthat nobody could fix...",
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .shimmer(cornerRadius = 4.dp),
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PostChip(
                text = "3/10",
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconResId = R.drawable.ic_mutations,
                modifier = Modifier.shimmer(cornerRadius = 50.dp)
            )

            PostChip(
                text = "60s",
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconResId = R.drawable.ic_clock,
                modifier = Modifier.shimmer(cornerRadius = 50.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview
@Composable
fun DescribeDrawingContentShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
        ) {
            DescribeDrawingContentShimmer()
        }
    }
}
