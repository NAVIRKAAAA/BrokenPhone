package com.brokentelephone.game.features.edit_avatar.content

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.avatar.AvatarComponent
import com.brokentelephone.game.core.composable.top_bar.AuthTopBar
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.edit_avatar.model.AvatarUi
import com.brokentelephone.game.features.edit_avatar.model.Avatars
import com.brokentelephone.game.features.edit_avatar.model.EditAvatarState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditAvatarContent(
    state: EditAvatarState,
    onBackClick: () -> Unit,
    onAvatarClick: (AvatarUi) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        AuthTopBar(
            title = stringResource(R.string.edit_avatar_title),
            onBackClick = onBackClick,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            state = gridState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stickyHeader {
                Text(
                    text = stringResource(R.string.edit_avatar_current),
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = 12.dp),
                    textAlign = TextAlign.Start
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Crossfade(
                    targetState = state.avatarUi,
                    animationSpec = tween(300),
                    label = "currentAvatar"
                ) { avatarUi ->
                    AvatarComponent(
                        avatarUrl = avatarUi?.url,
                        size = 120.dp,
                    )
                }
            }

            stickyHeader {
                Text(
                    text = stringResource(R.string.edit_avatar_choose),
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Start
                )
            }

            items(
                items = Avatars.all,
                key = { it.id }
            ) { avatar ->
                val isLoading = avatar.id == state.loadingAvatarId
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable(
                            enabled = state.loadingAvatarId == null,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onAvatarClick(avatar) }
                ) {
                    AvatarComponent(
                        avatarUrl = avatar.url,
                        size = Dp.Unspecified,
                        modifier = Modifier.fillMaxSize(),
                    )
                    if (isLoading) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                            )
                        }
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Preview
@Composable
fun EditAvatarContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        EditAvatarContent(
            state = EditAvatarState(avatarUi = Avatars.all[1]),
            onBackClick = {},
            onAvatarClick = {},
        )
    }
}
