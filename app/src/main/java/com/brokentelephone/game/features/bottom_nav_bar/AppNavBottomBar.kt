package com.brokentelephone.game.features.bottom_nav_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.coloredShadow
import com.brokentelephone.game.features.bottom_nav_bar.model.BottomNavBar
import com.brokentelephone.game.features.bottom_nav_bar.model.BottomNavBarEvent
import org.koin.compose.koinInject

@Composable
fun AppNavBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AppNavBottomBarViewModel = koinInject()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var isVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.let { entry ->
            isVisible = viewModel.shouldShowBottomBar(entry)
            if (isVisible) {
                viewModel.resetScrollVisibility()
                viewModel.updateSelectedItem(entry)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                BottomNavBarEvent.NavigateToCreate -> {
//                    navController.navigateSingle(Routes.CreatePost)
                }

                BottomNavBarEvent.NavigateToDashboard -> {
//                    navController.navigateSaved(Routes.Dashboard)
                }

                BottomNavBarEvent.NavigateToProfile -> {
//                    navController.navigateSaved(Routes.Profile)
                }

                else -> return@collect
            }
        }
    }

    Box(
        modifier = modifier,
    ) {
        AnimatedVisibility(
            visible = isVisible && state.isVisibleByScroll,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 300),
                initialOffsetY = { fullHeight -> fullHeight * 2 }
            ),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300),
                targetOffsetY = { fullHeight -> fullHeight * 2 }
            )
        ) {
            Row(
                modifier = Modifier
                    .coloredShadow(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.14f),
                        blurRadius = 16f,
                        offsetY = 0.dp,
                        offsetX = 0.dp,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavBar.entries.forEach { item ->
                    AppNavBottomBarItem(
                        item = item,
                        isSelected = item == state.selectedItem,
                        avatarUrl = if (item == BottomNavBar.PROFILE) state.userAvatarUrl else null,
                        isAuth = state.isAuth,
                        onClick = { viewModel.onItemClick(item) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AppNavBottomBarItem(
    item: BottomNavBar,
    isSelected: Boolean,
    isAuth: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
) {

    val color = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val iconSize = when (item) {
        BottomNavBar.DASHBOARD -> 20.dp
        BottomNavBar.CREATE -> 32.dp
        else -> 24.dp
    }

    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    val backgroundScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "backgroundScale"
    )
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(200),
        label = "backgroundAlpha"
    )

    val title = if (item == BottomNavBar.PROFILE && !isAuth) {
        stringResource(R.string.bottom_nav_bar_guest)
    } else {
        stringResource(item.titleResId)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .drawBehind {
                scale(backgroundScale) {
                    drawRoundRect(
                        color = primaryContainerColor.copy(alpha = backgroundAlpha),
                        cornerRadius = CornerRadius(28.dp.toPx())
                    )
                }
            }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (avatarUrl != null) {
            AvatarComponent(
                avatarUrl = avatarUrl,
                size = iconSize,
            )
        } else {
            Icon(
                painter = painterResource(item.iconResId),
                contentDescription = title,
                modifier = Modifier.size(iconSize),
                tint = color
            )
        }

        if (item != BottomNavBar.CREATE) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                color = color,
                lineHeight = 10.sp
            )
        }
    }
}

@Preview
@Composable
fun AppNavBottomBarPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(0.85f)
            ) {
                Row(
                    modifier = Modifier
                        .coloredShadow(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.14f),
                            blurRadius = 16f,
                            offsetY = 0.dp,
                            offsetX = 0.dp,
                            shape = RoundedCornerShape(28.dp)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(28.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavBar.entries.forEach { item ->
                        AppNavBottomBarItem(
                            item = item,
                            isSelected = item == BottomNavBar.DASHBOARD,
                            isAuth = false,
                            avatarUrl = null,
                            onClick = { {} },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}