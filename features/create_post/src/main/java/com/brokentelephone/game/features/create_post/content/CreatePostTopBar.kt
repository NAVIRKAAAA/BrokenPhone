package com.brokentelephone.game.features.create_post.content

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun CreatePostTopBar(
    isPostButtonEnabled: Boolean = true,
    onCloseClick: () -> Unit = {},
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val animatedVector = AnimatedImageVector.animatedVectorResource(R.drawable.ic_x_to_back)
    var isFirstRender by remember { mutableStateOf(true) }
    var atEnd by remember { mutableStateOf(false) }

    LaunchedEffect(showBackButton) {
        if (isFirstRender) {
            isFirstRender = false
            atEnd = showBackButton
        } else {
            atEnd = showBackButton
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
    ) {

        IconButton(
            onClick = {
                if (showBackButton) {
                    onBackClick()
                } else {
                    onCloseClick()
                }
            },
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painter = rememberAnimatedVectorPainter(animatedVector, atEnd),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Text(
            text = stringResource(R.string.create_post_title),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 18.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Center),
        )

        TextButton(
            onClick = onPostClick,
            enabled = isPostButtonEnabled,
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Text(
                text = stringResource(R.string.common_post),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
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
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            CreatePostTopBar()
        }
    }
}
