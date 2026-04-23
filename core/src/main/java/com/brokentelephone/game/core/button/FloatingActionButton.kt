package com.brokentelephone.game.core.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.modifier.coloredShadow
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun FloatingActionButton(
    imageResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    IconButton(
        onClick = onClick,
        modifier = modifier
            .coloredShadow(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.14f),
                blurRadius = 16f,
                offsetY = 0.dp,
                offsetX = 0.dp,
                shape = CircleShape,
            )
            .background(MaterialTheme.colorScheme.background, CircleShape),
        shape = CircleShape
    ) {
        Icon(
            painter = painterResource(imageResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }

}

@Preview
@Composable
fun FloatingActionButtonPreview() {
    BrokenTelephoneTheme() {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {

            FloatingActionButton(onClick = {}, imageResId = R.drawable.ic_back)

        }
    }
}