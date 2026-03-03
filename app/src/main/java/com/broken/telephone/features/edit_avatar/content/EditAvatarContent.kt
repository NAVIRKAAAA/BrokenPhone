package com.broken.telephone.features.edit_avatar.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        EditAvatarTopBar(
            isSaveEnabled = state.isSaveEnabled,
            onCloseClick = onBackClick,
            onSaveClick = onSaveClick,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.edit_avatar_current),
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(12.dp))

        AvatarComponent(
            avatarUrl = Avatars.all.find { it.id == state.selectedAvatarId }?.url,
            size = 120.dp,
            modifier = Modifier
                .padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.edit_avatar_choose),
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 14.sp,
            lineHeight = 21.sp,
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

                AvatarComponent(
                    avatarUrl = avatar.url,
                    size = Dp.Unspecified,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .then(
                            if (isSelected) Modifier.border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ) else Modifier
                        )
                        .clickable { onAvatarClick(avatar) }
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
    BrokenTelephoneTheme {
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
