package com.brokentelephone.game.features.describe_drawing.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.chip.PostChip
import com.brokentelephone.game.core.composable.post.DrawPostImage
import com.brokentelephone.game.core.composable.shimmer.ShimmerContent
import com.brokentelephone.game.core.composable.top_bar.SaveTopBar
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.features.describe_drawing.model.DescribeDrawingState
import kotlinx.coroutines.delay

@Composable
fun DescribeDrawingContent(
    state: DescribeDrawingState,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit = {},
    onPostClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {

    val post = state.postUi
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding(),
    ) {
        SaveTopBar(
            title = stringResource(R.string.describe_drawing_title),
            onBackClick = onBackClick,
            isSaveEnabled = state.text.isNotBlank() && !state.isTextOverLimit && !state.isTimerExpired && !state.isPosting,
            onSaveClick = onPostClick,
            isLoading = state.isPosting,
            saveButtonText = stringResource(R.string.common_post),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            ShimmerContent(
                isLoading = post == null,
                shimmerContent = {
                    DescribeDrawingContentShimmer()
                },
                content = {
                    if (post != null) {
                        LaunchedEffect(Unit) {
                            delay(150)
                            focusRequester.requestFocus()
                        }

                        Column {
                            val content =
                                post.content as? PostContent.Drawing ?: return@ShimmerContent

                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                val imageSize = minOf(maxWidth, maxHeight)
                                DrawPostImage(
                                    content = content,
                                    modifier = Modifier
                                        .size(imageSize)
                                        .clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            HorizontalDivider(color = MaterialTheme.appColors.divider)

                            Spacer(modifier = Modifier.height(16.dp))

                            CompositionLocalProvider(
                                LocalTextSelectionColors provides TextSelectionColors(
                                    handleColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                            ) {
                                BasicTextField(
                                    value = state.text,
                                    onValueChange = onTextChanged,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .focusRequester(focusRequester),
                                    textStyle = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                        fontSize = 15.sp,
                                        lineHeight = 22.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    ),
                                    minLines = 2,
                                    maxLines = 3,
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                            onPostClick()
                                        }
                                    ),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                ) { innerTextField ->
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        if (state.text.isEmpty()) {
                                            Text(
                                                text = stringResource(R.string.describe_drawing_placeholder),
                                                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                                fontSize = 15.sp,
                                                lineHeight = 22.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
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
                                    modifier = Modifier,
                                )

                                PostChip(
                                    text = state.formattedTime,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    iconResId = R.drawable.ic_clock,
                                    modifier = Modifier,
                                )


                                if (state.text.isNotBlank()) {
                                    val isTextOverLimit = state.isTextOverLimit

                                    val counterContentColor =
                                        if (isTextOverLimit) MaterialTheme.colorScheme.onErrorContainer
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    val counterContainerColor =
                                        if (isTextOverLimit) MaterialTheme.colorScheme.errorContainer
                                        else MaterialTheme.colorScheme.surfaceVariant

                                    PostChip(
                                        text = "${state.text.length}/${DescribeDrawingState.MAX_TEXT_LENGTH}",
                                        contentColor = counterContentColor,
                                        containerColor = counterContainerColor,
                                        iconResId = null,
                                        modifier = Modifier,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            )
        }
    }

}

@Preview
@Composable
fun DescribeDrawingContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DescribeDrawingContent(
                state = DescribeDrawingState(
                    text = "",
                    postUi = PostUi(
                        id = "2",
                        authorId = "user-1",
                        authorName = "Alex",
                        avatarUrl = null,
                        content = PostContent.Drawing("Once upon a time there was a broken telephone that nobody could fix..."),
                        createdAt = System.currentTimeMillis() - 7200000,
                        generation = 10,
                        maxGenerations = 10,
                        status = PostStatus.AVAILABLE,
                        drawingTimeLimit = 60,
                        textTimeLimit = 60
                    )
                )
            )
        }
    }
}