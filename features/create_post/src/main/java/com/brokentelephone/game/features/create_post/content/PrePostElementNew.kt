package com.brokentelephone.game.features.create_post.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.avatar.AvatarComponent
import com.brokentelephone.game.core.composable.chip.PostChip
import com.brokentelephone.game.core.composable.draw.DrawingCanvas
import com.brokentelephone.game.core.model.tab_row.create_post.CreatePostTab
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.toShortFormattedTime
import com.brokentelephone.game.features.create_post.model.ChainSetting
import com.brokentelephone.game.features.create_post.model.CreatePostState

@Composable
fun PrePostElementNew(
    name: String,
    text: String,
    onTextChanged: (String) -> Unit,
    onChainSettingClick: (ChainSetting) -> Unit,
    onDone: () -> Unit,
    isTextOverLimit: Boolean,
    selectedTab: CreatePostTab,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    avatarUrl: String? = null,
    maxGenerations: Int = 0,
    textTimeLimit: Int = 0,
    drawingTimeLimit: Int = 0,
) {
    val focusManager = LocalFocusManager.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentWidth = screenWidth - 32.dp // 16dp * 2
    val density = LocalDensity.current

    var textFieldHeight by remember { mutableStateOf(0.dp) }
    val avatarSize = 40.dp
    val contentPadding = 16.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Box() {
            Row(
                modifier = Modifier
                    .padding(horizontal = contentPadding)
                    .padding(top = contentPadding)
            ) {
                AvatarComponent(
                    avatarUrl = avatarUrl,
                    size = avatarSize,
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = name,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.graphicsLayer {
                        val f = pagerState.currentPage + pagerState.currentPageOffsetFraction
                        translationY = f * (avatarSize - 24.sp.toDp()).toPx() / 2f
                    },
                )

            }

            Column() {

                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth(),
                    beyondViewportPageCount = CreatePostTab.entries.size,
                    userScrollEnabled = false
                ) {

                    when (CreatePostTab.entries[it]) {
                        CreatePostTab.TEXT -> {

                            Column(modifier = Modifier) {
                                Spacer(modifier = Modifier.height(48.dp))

                                CompositionLocalProvider(
                                    LocalTextSelectionColors provides TextSelectionColors(
                                        handleColor = MaterialTheme.colorScheme.primary,
                                        backgroundColor = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.3f
                                        ),
                                    )
                                ) {
                                    BasicTextField(
                                        value = text,
                                        onValueChange = onTextChanged,
                                        modifier = Modifier
                                            .graphicsLayer {
                                                val f = pagerState.currentPage + pagerState.currentPageOffsetFraction
                                                alpha = 1f - f
                                                translationY = f * (avatarSize - 24.sp.toDp()).toPx() / 2f
                                            }
                                            .onSizeChanged { size ->
                                                textFieldHeight =
                                                    with(density) { size.height.toDp() }
                                            }
                                            .padding(start = 68.dp)
                                            .fillMaxWidth()
                                            .then(
                                                focusRequester?.let { Modifier.focusRequester(it) }
                                                    ?: Modifier
                                            ),
                                        textStyle = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                            fontSize = 15.sp,
                                            lineHeight = 22.sp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                        ),
                                        minLines = 3,
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus()
                                                onDone()
                                            }
                                        ),
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                    ) { innerTextField ->
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            if (text.isEmpty()) {
                                                Text(
                                                    text = stringResource(R.string.create_post_placeholder),
                                                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                                    fontSize = 15.sp,
                                                    lineHeight = 22.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                        alpha = 0.8f
                                                    ),
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                }
                            }

                        }

                        CreatePostTab.DRAW -> {
                            Column {
                                Spacer(
                                    modifier = Modifier.layout { measurable, constraints ->
                                        val f = pagerState.currentPage + pagerState.currentPageOffsetFraction
                                        val heightPx = (68.dp.toPx() * f).toInt().coerceAtLeast(0)
                                        val placeable = measurable.measure(constraints)
                                        layout(placeable.width, heightPx) {
                                            placeable.placeRelative(0, 0)
                                        }
                                    }
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .padding(horizontal = contentPadding)
                                        .clip(RoundedCornerShape(14.dp))
                                        .graphicsLayer {
                                            val fraction =
                                                pagerState.currentPage + pagerState.currentPageOffsetFraction
                                            alpha = fraction
                                        },
                                ) {
                                    DrawingCanvas(
                                        paths = emptyList(),
                                        currentPath = null,
                                        onAction = {},
                                        enabled = false,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = contentPadding)
                        .layout { measurable, constraints ->
                            val f = pagerState.currentPage + pagerState.currentPageOffsetFraction
                            val offsetPx = ((1f - f) * 52.dp.toPx()).toInt().coerceAtLeast(0)
                            val placeable = measurable.measure(
                                constraints.copy(maxWidth = (constraints.maxWidth - offsetPx).coerceAtLeast(constraints.minWidth))
                            )
                            layout(constraints.maxWidth, placeable.height) {
                                placeable.placeRelative(offsetPx, 0)
                            }
                        }
                        .graphicsLayer {
                            val f = pagerState.currentPage + pagerState.currentPageOffsetFraction
                            val startY = (textFieldHeight + contentPadding).toPx()
                            val endY = contentWidth.toPx()
                            translationY = (1f - f) * (startY - endY)
                        },
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {

                    PostChip(
                        text = stringResource(
                            R.string.create_post_badge_generations,
                            maxGenerations
                        ),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconResId = R.drawable.ic_mutations,
                        modifier = Modifier,
                        enabled = true,
                        onClick = { onChainSettingClick(ChainSetting.CHAIN_LENGTH) }
                    )

                    PostChip(
                        text = textTimeLimit.toShortFormattedTime(),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconResId = R.drawable.ic_edit_v2,
                        modifier = Modifier,
                        enabled = true,
                        onClick = { onChainSettingClick(ChainSetting.TEXT_TIME) }
                    )

                    PostChip(
                        text = drawingTimeLimit.toShortFormattedTime(),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconResId = R.drawable.ic_brush_v2,
                        modifier = Modifier,
                        enabled = true,
                        onClick = { onChainSettingClick(ChainSetting.DRAWING_TIME) }
                    )


                    if (text.isNotBlank() && selectedTab == CreatePostTab.TEXT) {
                        val counterContentColor =
                            if (isTextOverLimit) MaterialTheme.colorScheme.onErrorContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        val counterContainerColor =
                            if (isTextOverLimit) MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.surfaceVariant

                        PostChip(
                            text = stringResource(
                                R.string.create_post_text_counter,
                                text.length,
                                CreatePostState.MAX_TEXT_LENGTH
                            ),
                            contentColor = counterContentColor,
                            containerColor = counterContainerColor,
                            iconResId = null,
                            modifier = Modifier,
                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrePostElementNewTextTabPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        PrePostElementNew(
            name = "Alex",
            text = "Test",
            onTextChanged = {},
            onChainSettingClick = {},
            onDone = {},
            isTextOverLimit = false,
            pagerState = rememberPagerState(0) { 2 },
            maxGenerations = 10,
            textTimeLimit = 60,
            drawingTimeLimit = 120,
            selectedTab = CreatePostTab.TEXT
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrePostElementNewDrawTabPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        PrePostElementNew(
            name = "Alex",
            text = "",
            onTextChanged = {},
            onChainSettingClick = {},
            onDone = {},
            isTextOverLimit = false,
            pagerState = rememberPagerState(1) { 2 },
            maxGenerations = 10,
            textTimeLimit = 60,
            drawingTimeLimit = 120,
            selectedTab = CreatePostTab.DRAW
        )
    }
}
