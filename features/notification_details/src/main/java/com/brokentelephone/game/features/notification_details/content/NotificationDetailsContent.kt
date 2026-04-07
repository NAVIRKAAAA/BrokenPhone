package com.brokentelephone.game.features.notification_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.notification_details.model.NotificationDetailsState

@Composable
fun NotificationDetailsContent(
    state: NotificationDetailsState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {

    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationDetailsContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        NotificationDetailsContent(
            state = NotificationDetailsState(),
            onBackClick = {},
        )
    }
}
