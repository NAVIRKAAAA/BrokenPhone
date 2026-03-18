package com.brokentelephone.game.features.describe_drawing.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.badge.BadgeElement
import com.brokentelephone.game.core.post.DrawPostImage
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.core.top_bar.PostTopBar
import com.brokentelephone.game.data.repository.MockPostRepository
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.features.create_post.model.CreatePostState
import com.brokentelephone.game.features.dashboard.model.toUi
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

    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding(),
    ) {
        PostTopBar(
            title = stringResource(R.string.describe_drawing_title),
            onBackClick = onBackClick,
            isPostButtonEnabled = state.text.isNotBlank() && !state.isTextOverLimit && !state.isTimerExpired,
            onPostClick = onPostClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            if (post != null) {
                val content = post.content as? PostContent.Drawing ?: return@Column

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
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(14.dp)
                            ),
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
                        minLines = 3,
                        maxLines = 3,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                onPostClick()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
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

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    BadgeElement(
                        iconResId = R.drawable.ic_mutations,
                        text = stringResource(
                            R.string.create_post_badge_generations,
                            post.maxGenerations
                        ),
                    )

                    BadgeElement(
                        iconResId = R.drawable.ic_clock,
                        text = state.formattedTime,
                    )

                    if (state.text.isNotBlank()) {

                        Text(
                            text = "|",
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.appColors.divider
                        )


                        Text(
                            text = "${state.text.length}/${CreatePostState.MAX_TEXT_LENGTH}",
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = if (state.isTextOverLimit) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

@Preview
@Composable
fun DescribeDrawingContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DescribeDrawingContent(
                state = DescribeDrawingState(
                    text = "",
                    postUi = MockPostRepository.mockList.last().toUi()
                )
            )
        }
    }
}