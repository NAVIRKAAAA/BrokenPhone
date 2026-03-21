package com.brokentelephone.game.core.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun EmptyDrawPost(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Icon(
//                painter = painterResource(R.drawable.ic_brush),
//                contentDescription = null,
//                modifier = Modifier.size(48.dp),
//                tint = MaterialTheme.colorScheme.error
//            )
//
//            Spacer(modifier = Modifier.height(2.dp))
//
//            Text(
//                text = "Error during download",
//                textAlign = TextAlign.Center,
//                fontFamily = FontFamily(Font(R.font.inter_medium)),
//                fontSize = 14.sp,
//                lineHeight = 24.sp,
//                color = MaterialTheme.colorScheme.error
//            )
        }


    }

}

@Preview
@Composable
fun EmptyDrawPostPreview() {
    BrokenTelephoneTheme() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .height(200.dp)
        ) {
            EmptyDrawPost()
        }
    }
}