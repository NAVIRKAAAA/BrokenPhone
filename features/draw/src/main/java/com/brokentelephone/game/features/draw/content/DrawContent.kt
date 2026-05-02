package com.brokentelephone.game.features.draw.content

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.chip.PostChip
import com.brokentelephone.game.core.composable.draw.DrawBottomBar
import com.brokentelephone.game.core.composable.shimmer.ShimmerContent
import com.brokentelephone.game.core.composable.top_bar.SaveTopBar
import com.brokentelephone.game.core.ext.modifier.horizontalFadingEdge
import com.brokentelephone.game.core.model.draw.DrawingCanvas
import com.brokentelephone.game.core.model.draw.DrawingCanvasAction
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.core.utils.toFormattedTime
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.features.draw.model.DrawState
import com.brokentelephone.game.features.draw.model.DrawingAction

@Composable
fun DrawContent(
    state: DrawState,
    modifier: Modifier = Modifier,
    onDrawAction: (DrawingAction) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val post = state.postUi
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        SaveTopBar(
            title = stringResource(R.string.draw_title),
            onBackClick = onBackClick,
            isSaveEnabled = state.canUndo && !state.isTimerExpired && !state.isPosting,
            onSaveClick = { onDrawAction(DrawingAction.OnPostClick) },
            isLoading = state.isPosting,
            saveButtonText = stringResource(R.string.common_post),
        )

        Spacer(modifier = Modifier.height(12.dp))

        ShimmerContent(
            isLoading = post == null,
            shimmerContent = {
                DrawContentShimmer()
            },
            content = {
                if (post != null) {
                    val content = post.content as? PostContent.Text ?: return@ShimmerContent
                    Column {
                        Text(
                            text = content.text,
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            modifier = Modifier
                                .horizontalFadingEdge()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            minLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        HorizontalDivider(color = MaterialTheme.appColors.divider)

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(14.dp))
                        ) {
                            DrawingCanvas(
                                paths = state.paths,
                                currentPath = state.currentPath,
                                onAction = { onDrawAction(DrawingAction.OnCanvasAction(it)) },
                                enabled = !state.isTimerExpired,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            itemVerticalAlignment = Alignment.CenterVertically
                        ) {
                            PostChip(
                                text = "${post.generation + 1}/${post.maxGenerations}",
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                iconResId = R.drawable.ic_mutations,
                            )

                            PostChip(
                                text = state.remainingSeconds.toFormattedTime(),
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                iconResId = R.drawable.ic_clock,
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Spacer(modifier = Modifier.weight(1f))

                        HorizontalDivider(color = MaterialTheme.appColors.divider)

                        DrawBottomBar(
                            selectedBrushSize = state.selectedBrushSize,
                            selectedColor = state.selectedColor,
                            canUndo = state.canUndo,
                            canRedo = state.canRedo,
                            canClear = state.canUndo,
                            onUndo = { onDrawAction(DrawingAction.OnCanvasAction(DrawingCanvasAction.OnUndoClick)) },
                            onRedo = { onDrawAction(DrawingAction.OnCanvasAction(DrawingCanvasAction.OnRedoClick)) },
                            onClear = {
                                onDrawAction(
                                    DrawingAction.OnCanvasAction(
                                        DrawingCanvasAction.OnClearCanvasClick
                                    )
                                )
                            },
                            onBrushSizeChange = {
                                onDrawAction(
                                    DrawingAction.OnCanvasAction(
                                        DrawingCanvasAction.OnBrushSizeChange(it)
                                    )
                                )
                            },
                            onColorChange = {
                                onDrawAction(
                                    DrawingAction.OnCanvasAction(
                                        DrawingCanvasAction.OnColorChange(it)
                                    )
                                )
                            },
                            modifier = Modifier.navigationBarsPadding()
                        )
                    }
                }
            }
        )

    }

}

@Preview
@Composable
fun DrawContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DrawContent(
                state = DrawState(
                    postUi = PostUi(
                        id = "2",
                        authorId = "user-1",
                        authorName = "Alex",
                        avatarUrl = null,
                        content = PostContent.Text("Once upon a time there was a broken telephone that nobody could fix..."),
                        createdAt = System.currentTimeMillis() - 7200000,
                        generation = 9,
                        maxGenerations = 10,
                        status = PostStatus.AVAILABLE,
                        drawingTimeLimit = 60,
                        textTimeLimit = 60
                    )
                ),
            )
        }
    }
}