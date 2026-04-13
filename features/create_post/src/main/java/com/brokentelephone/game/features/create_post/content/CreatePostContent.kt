package com.brokentelephone.game.features.create_post.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.create_post.model.ChainSetting
import com.brokentelephone.game.features.create_post.model.CreatePostState
import kotlinx.coroutines.delay

@Composable
fun CreatePostContent(
    state: CreatePostState,
    onTextChanged: (String) -> Unit,
    onChainSettingClick: (ChainSetting) -> Unit,
    onPostClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        CreatePostTopBar(
            isPostButtonEnabled = state.text.isNotBlank() && !state.isTextOverLimit,
            onCloseClick = onBackClick,
            onPostClick = {
                focusManager.clearFocus()
                onPostClick()
            },
        )

        PrePostElement(
            name = state.user?.username.orEmpty(),
            text = state.text,
            avatarUrl = state.user?.avatarUrl,
            onTextChanged = onTextChanged,
            onChainSettingClick = onChainSettingClick,
            isTextOverLimit = state.isTextOverLimit,
            maxGenerations = state.maxGenerations,
            textTimeLimit = state.textTimeLimit,
            drawingTimeLimit = state.drawingTimeLimit,
            focusRequester = focusRequester,
            onDone = onPostClick
        )
    }

}

@Preview
@Composable
fun CreatePostContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        CreatePostContent(
            state = CreatePostState(
                user = UserUi(
                    id = "0",
                    username = "Alex",
                    email = "",
                    avatarUrl = "",
                    createdAt = 0
                ),
//                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed lectus massa, gravida quis efficitur ut, vehicula id nulla. Phasellus placerat odio id tortor efficitur lacinia. Quisque a semper ante. In hac habitasse platea dictumst. Proin ut euismod massa. Sed sodales nibh purus, in consequat quam feugiat vitae. Curabitur scelerisque massa ac consequat luctus. In tincidunt blandit felis. In sed nulla diam. Nullam a auctor felis, ut pretium lacus.",
            ),
            onTextChanged = {},
            onChainSettingClick = {},
            onBackClick = {},
            onPostClick = {}
        )
    }
}