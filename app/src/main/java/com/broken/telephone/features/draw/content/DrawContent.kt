package com.broken.telephone.features.draw.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.badge.BadgeElement
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.core.theme.appColors
import com.broken.telephone.core.top_bar.PostTopBar
import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.features.dashboard.model.toUi
import com.broken.telephone.features.draw.model.DrawState
import com.broken.telephone.features.draw.model.DrawingAction

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
        PostTopBar(
            title = stringResource(R.string.draw_title),
            onBackClick = onBackClick,
            isPostButtonEnabled = state.canUndo && !state.isTimerExpired,
            onPostClick = { onDrawAction(DrawingAction.OnPostClick) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (post != null) {
            val content = post.content as? PostContent.Text ?: return@Column

            Text(
                text = content.text,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface,
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
                    .border(1.dp, MaterialTheme.appColors.divider, RoundedCornerShape(14.dp))
            ) {
                DrawingCanvas(
                    paths = state.paths,
                    currentPath = state.currentPath,
                    onAction = onDrawAction,
                    enabled = !state.isTimerExpired,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                BadgeElement(
                    iconResId = R.drawable.ic_mutations,
                    text = "${post.generation}/${post.maxGenerations}",
                )

                BadgeElement(
                    iconResId = R.drawable.ic_clock,
                    text = state.formattedTime,
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider(color = MaterialTheme.appColors.divider)

            DrawBottomBar(
                selectedBrushSize = state.selectedBrushSize,
                canUndo = state.canUndo,
                canRedo = state.canRedo,
                canClear = state.canUndo,
                onUndo = { onDrawAction(DrawingAction.OnUndoClick) },
                onRedo = { onDrawAction(DrawingAction.OnRedoClick) },
                onClear = { onDrawAction(DrawingAction.OnClearCanvasClick) },
                onBrushSizeChange = { onDrawAction(DrawingAction.OnBrushSizeChange(it)) },
                modifier = Modifier.navigationBarsPadding()
            )

        }

    }

}

@Preview
@Composable
fun DrawContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        ) {
            DrawContent(
                state = DrawState(
                    MockPostRepository.mockList[1].toUi()
                ),
            )
        }
    }
}