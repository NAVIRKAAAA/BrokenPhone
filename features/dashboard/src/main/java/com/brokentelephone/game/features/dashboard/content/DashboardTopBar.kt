package com.brokentelephone.game.features.dashboard.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.modifier.coloredShadow
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun DashboardTopBar(
    name: String,
    isScrolled: Boolean,
    onTitleClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier,
    unreadNotificationsCount: Int = 0,
) {
    val greeting = if (name.isBlank()) stringResource(R.string.dashboard_title_no_name)
                   else stringResource(R.string.dashboard_title, name)

    val blurRadius by animateFloatAsState(
        targetValue = if (isScrolled) 16f else 0f,
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTitleClick,
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Text(
            text = greeting,
            textAlign = TextAlign.Start,
            fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
            fontSize = 20.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
                .padding(end = 8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    role = Role.Button,
                    onClick = onTitleClick,
                ),
        )

        IconButton(
            onClick = onNotificationsClick,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            BadgedBox(
                badge = {
                    if (unreadNotificationsCount > 0) {
                        Badge()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_notifications),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Preview
@Composable
private fun DashboardTopBarPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            DashboardTopBar(
                name = "Alex".repeat(44),
                isScrolled = false,
                onTitleClick = {},
                onNotificationsClick = {},
                unreadNotificationsCount = 44
            )
        }
    }
}

@Preview
@Composable
private fun DashboardTopBarScrolledPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            DashboardTopBar(
                name = "",
                isScrolled = true,
                onTitleClick = {},
                onNotificationsClick = {},
            )
        }
    }
}
