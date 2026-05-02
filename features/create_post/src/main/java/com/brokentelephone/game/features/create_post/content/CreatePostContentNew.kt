package com.brokentelephone.game.features.create_post.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.composable.draw.DrawBottomBar
import com.brokentelephone.game.core.composable.profile.tab_row.ProfileTabRowNewVTwo
import com.brokentelephone.game.core.model.draw.DrawingCanvasAction
import com.brokentelephone.game.core.model.tab_row.create_post.CreatePostTab
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.features.create_post.model.ChainSetting
import com.brokentelephone.game.features.create_post.model.CreatePostState
import kotlinx.coroutines.delay

@Composable
fun CreatePostContentNew(
    state: CreatePostState,
    onTextChanged: (String) -> Unit,
    onChainSettingClick: (ChainSetting) -> Unit,
    onPostClick: () -> Unit,
    modifier: Modifier = Modifier,
    onTabSelect: (CreatePostTab) -> Unit,
    onDrawAction: (DrawingCanvasAction) -> Unit,
    onCloseClick: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val pagerState =
        rememberPagerState(initialPage = state.selectedTab.ordinal) { CreatePostTab.entries.size }

    BackHandler(state.selectedTab == CreatePostTab.DRAW) {
        onTabSelect(CreatePostTab.TEXT)
        focusRequester.requestFocus()
    }

    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
    }

    LaunchedEffect(state.selectedTab) {
        pagerState.animateScrollToPage(state.selectedTab.ordinal)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CreatePostTopBar(
            isPostButtonEnabled = state.isSubmitButtonEnabled,
            onCloseClick = onCloseClick,
            showBackButton = state.selectedTab == CreatePostTab.DRAW,
            onBackClick = {
                onTabSelect(CreatePostTab.TEXT)
                focusRequester.requestFocus()
            },
            onPostClick = {
                focusManager.clearFocus()
                onPostClick()
            },
        )

        ProfileTabRowNewVTwo(
            tabs = CreatePostTab.entries,
            selectedIndex = state.selectedTab.ordinal,
            onTabSelect = {
                onTabSelect(it)

                if (it == CreatePostTab.DRAW) {
                    focusManager.clearFocus()
                }
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .clipToBounds()
                .layout { measurable, constraints ->
                    val f = pagerState.currentPage + pagerState.currentPageOffsetFraction
                    val placeable = measurable.measure(constraints)
                    val height = (placeable.height * (1f - f)).toInt().coerceAtLeast(0)
                    layout(placeable.width, height) {
                        placeable.placeRelative(0, height - placeable.height)
                    }
                }
        )


        PrePostElementNew(
            state = state,
            pagerState = pagerState,
            onTextChanged = onTextChanged,
            onChainSettingClick = onChainSettingClick,
            focusRequester = focusRequester,
            onDone = onPostClick,
            onDrawAction = onDrawAction,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.graphicsLayer {
                val f = pagerState.currentPage + pagerState.currentPageOffsetFraction
                translationY = (1f - f) * size.height
            }
        ) {
            HorizontalDivider(color = MaterialTheme.appColors.divider)

            DrawBottomBar(
                selectedBrushSize = state.selectedBrushSize,
                selectedColor = state.selectedColor,
                canUndo = state.canUndo,
                canRedo = state.canRedo,
                canClear = state.canUndo,
                onUndo = { onDrawAction(DrawingCanvasAction.OnUndoClick) },
                onRedo = { onDrawAction(DrawingCanvasAction.OnRedoClick) },
                onClear = { onDrawAction(DrawingCanvasAction.OnClearCanvasClick) },
                onBrushSizeChange = { onDrawAction(DrawingCanvasAction.OnBrushSizeChange(it)) },
                onColorChange = { onDrawAction(DrawingCanvasAction.OnColorChange(it)) },
                modifier = Modifier.navigationBarsPadding(),
            )
        }
    }

}

@Preview
@Composable
fun CreatePostContentNewPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        CreatePostContentNew(
            state = CreatePostState(
                user = UserUi(
                    id = "0",
                    username = "Alex",
                    email = "",
                    avatarUrl = "",
                    createdAt = 0
                ),
                selectedTab = CreatePostTab.TEXT
//                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed lectus massa, gravida quis efficitur ut, vehicula id nulla. Phasellus placerat odio id tortor efficitur lacinia. Quisque a semper ante. In hac habitasse platea dictumst. Proin ut euismod massa. Sed sodales nibh purus, in consequat quam feugiat vitae. Curabitur scelerisque massa ac consequat luctus. In tincidunt blandit felis. In sed nulla diam. Nullam a auctor felis, ut pretium lacus.",
            ),
            onTextChanged = {},
            onChainSettingClick = {},
            onCloseClick = {},
            onPostClick = {},
            onTabSelect = {},
            onDrawAction = {}
        )
    }
}