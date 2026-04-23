package com.brokentelephone.game.core.top_bar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.modifier.coloredShadow
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun EditProfileTopBar(
    title: String,
    showShadow: Boolean = false,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    actions: (@Composable () -> Unit)? = null,
) {
    val blurRadius by animateFloatAsState(
        targetValue = if (showShadow) 16f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "topBarBlur"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .coloredShadow(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.14f),
                blurRadius = blurRadius,
                offsetY = 0.dp,
                offsetX = 0.dp,
                shape = RoundedCornerShape(0.dp)
            )
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
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

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 18.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.primary,
        )

        if (actions != null) {
            actions()
        } else {
            Box(modifier = Modifier.size(48.dp))
        }
    }
}

@Preview
@Composable
private fun EditProfileTopBarPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            EditProfileTopBar(title = "Edit Profile")
        }
    }
}
