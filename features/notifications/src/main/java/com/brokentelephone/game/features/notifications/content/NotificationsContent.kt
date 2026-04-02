package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.notifications.model.NotificationsState

@Composable
fun NotificationsContent(
    state: NotificationsState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        NotificationsTopBar(
            title = stringResource(R.string.notifications_title),
            onCloseClick = onBackClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        NotificationsContent(
            state = NotificationsState(),
            onBackClick = {},
        )
    }
}
