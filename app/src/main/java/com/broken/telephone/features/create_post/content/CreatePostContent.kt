package com.broken.telephone.features.create_post.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.features.create_post.model.CreatePostState

@Composable
fun CreatePostContent(
    state: CreatePostState,
    onTextChanged: (String) -> Unit,
    onBadgeClick: () -> Unit,
    onPostClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        CreatePostTopBar(
            isPostButtonEnabled = state.text.isNotBlank() && !state.isTextOverLimit,
            onCloseClick = onBackClick,
            onPostClick = onPostClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrePostElement(
            name = "Alex",
            text = state.text,
            avatarUrl = state.avatarUrl,
            onTextChanged = onTextChanged,
            onBadgeClick = onBadgeClick,
            isTextOverLimit = state.isTextOverLimit,
            maxGenerations = state.maxGenerations,
            textTimeLimit = state.textTimeLimit,
            drawingTimeLimit = state.drawingTimeLimit,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }

}

@Preview
@Composable
fun CreatePostContentPreview() {
    CreatePostContent(
        state = CreatePostState(),
        onTextChanged = {},
        onBadgeClick = {},
        onBackClick = {},
        onPostClick = {}
    )
}