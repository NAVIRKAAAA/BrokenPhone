package com.broken.telephone.features.describe_drawing.content

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.broken.telephone.R
import com.broken.telephone.core.badge.BadgeElement
import com.broken.telephone.core.top_bar.PostTopBar
import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.features.create_post.model.CreatePostState
import com.broken.telephone.features.dashboard.model.toUi
import com.broken.telephone.features.describe_drawing.model.DescribeDrawingState
import com.broken.telephone.features.draw.model.DrawingAction
import java.io.File

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
    ) {
        PostTopBar(
            title = "Describe",
            onBackClick = onBackClick,
            isPostButtonEnabled = state.text.isNotBlank() && !state.isTextOverLimit,
            onPostClick = onPostClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            if (post != null) {
                val content = post.content as? PostContent.Drawing ?: return@Column

                val model = content.localPath?.let { File(it) } ?: content.imageUrl

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .aspectRatio(1f)
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                ) {
                    AsyncImage(
                        model = model,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(16.dp))

                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = Color.Black,
                        backgroundColor = Color.Black.copy(alpha = 0.3f)
                    )
                ) {
                    BasicTextField(
                        value = state.text,
                        onValueChange = onTextChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        textStyle = TextStyle(
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        ),
                        minLines = 3,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        cursorBrush = SolidColor(Color.Black),
                    ) { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (state.text.isEmpty()) {
                                Text(
                                    text = "Describe what you see...",
                                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp,
                                    color = Color.Gray
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
                        text = "0/${post.maxGenerations}",
                    )

                    BadgeElement(
                        iconResId = R.drawable.ic_clock,
                        text = "${post.content.timeLimit}s",
                    )

                    if (state.text.isNotBlank()) {

                        Text(
                            text = "|",
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )


                        Text(
                            text = "${state.text.length}/${CreatePostState.MAX_TEXT_LENGTH}",
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = if (state.isTextOverLimit) MaterialTheme.colorScheme.error else Color.Gray
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
    DescribeDrawingContent(
        state = DescribeDrawingState(
            text = "Lalala",
            postUi = MockPostRepository.mockList.last().toUi()
        )
    )
}