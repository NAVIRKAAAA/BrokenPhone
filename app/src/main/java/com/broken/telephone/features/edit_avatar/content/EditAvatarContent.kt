package com.broken.telephone.features.edit_avatar.content

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.avatar.AvatarComponent
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.core.top_bar.SaveTopBar
import com.broken.telephone.features.edit_avatar.model.AvatarUi
import com.broken.telephone.features.edit_avatar.model.Avatars
import com.broken.telephone.features.edit_avatar.model.EditAvatarState

@Composable
fun EditAvatarContent(
    state: EditAvatarState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onAvatarClick: (AvatarUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        SaveTopBar(
            title = stringResource(R.string.edit_avatar_title),
            saveButtonText = stringResource(R.string.edit_avatar_button_save),
            isSaveEnabled = state.isSaveEnabled,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.edit_avatar_current),
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(12.dp))

        Crossfade(
            targetState = Avatars.all.find { it.id == state.selectedAvatarId }?.url,
            animationSpec = tween(300),
            label = "currentAvatar"
        ) { avatarUrl ->
            AvatarComponent(
                avatarUrl = avatarUrl,
                size = 120.dp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.edit_avatar_choose),
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Start
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = Avatars.all,
                key = { it.id }
            ) { avatar ->
                val isSelected = avatar.id == state.selectedAvatarId
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.1f else 1f,
                    animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = 400f
                    ),
                    label = "avatarScale"
                )
                val borderWidth by animateDpAsState(
                    targetValue = if (isSelected) 3.dp else 0.dp,
                    animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = 400f
                    ),
                    label = "avatarBorder"
                )

                AvatarComponent(
                    avatarUrl = avatar.url,
                    size = Dp.Unspecified,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .scale(scale)
                        .clip(CircleShape)
                        .then(
                            if (isSelected) Modifier.border(
                                width = borderWidth,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ) else Modifier
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onAvatarClick(avatar) }
                )
            }

            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Preview
@Composable
fun EditAvatarContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        EditAvatarContent(
            state = EditAvatarState(
                selectedAvatarId = Avatars.all[1].id
            ),
            onBackClick = {},
            onSaveClick = {},
            onAvatarClick = {},
        )
    }
}
