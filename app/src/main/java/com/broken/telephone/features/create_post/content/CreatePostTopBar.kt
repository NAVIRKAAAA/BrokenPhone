package com.broken.telephone.features.create_post.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme

@Composable
fun CreatePostTopBar(
    isPostButtonEnabled: Boolean = true,
    onCloseClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(
            onClick = onCloseClick
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
            )

        }

        Text(
            text = "New Post",
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )

        TextButton(
            onClick = onPostClick,
            enabled = isPostButtonEnabled,
        ) {
            Text(
                text = "Post",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 16.sp,
                lineHeight = 24.sp,
            )
        }

    }

}

@Preview
@Composable
fun CreatePostTopBarPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier
        ) {
            CreatePostTopBar()
        }
    }
}