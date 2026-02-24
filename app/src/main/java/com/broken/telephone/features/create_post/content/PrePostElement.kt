package com.broken.telephone.features.create_post.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.broken.telephone.R
import com.broken.telephone.core.badge.BadgeElement
import com.broken.telephone.features.create_post.model.CreatePostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrePostElement(
    name: String,
    text: String,
    onTextChanged: (String) -> Unit,
    onBadgeClick: () -> Unit,
    isTextOverLimit: Boolean,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    avatarUrl: String? = null,
    maxGenerations: Int = 0,
    textTimeLimit: Int = 0,
    drawingTimeLimit: Int = 0,
) {

    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier.fillMaxWidth()
    ) {

        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            CompositionLocalProvider(
                LocalTextSelectionColors provides TextSelectionColors(
                    handleColor = Color.Black,
                    backgroundColor = Color.Black.copy(alpha = 0.3f)
                )
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = onTextChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier),
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
                        if (text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.create_post_placeholder),
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
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {

                BadgeElement(
                    iconResId = R.drawable.ic_mutations,
                    text = stringResource(R.string.create_post_badge_generations, maxGenerations),
                    onClick = onBadgeClick,
                )

                Spacer(modifier = Modifier.width(8.dp))

                BadgeElement(
                    iconResId = R.drawable.ic_clock,
                    text = stringResource(R.string.create_post_badge_time_limits, textTimeLimit, drawingTimeLimit),
                    onClick = onBadgeClick,
                )
                

                if (text.isNotBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "|",
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(R.string.create_post_text_counter, text.length, CreatePostState.MAX_TEXT_LENGTH),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = if (isTextOverLimit) MaterialTheme.colorScheme.error else Color.Gray
                    )
                }
            }
        }


    }

}

@Preview
@Composable
fun PrePostElementPreview() {
    PrePostElement(
        name = "Alex",
        text = "Дфдфдф",
//        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed lectus massa, gravida quis efficitur ut, vehicula id nulla. Phasellus placerat odio id tortor efficitur lacinia. Quisque a semper ante. In hac habitasse platea dictumst. Proin ut euismod massa. Sed sodales nibh purus, in consequat quam feugiat vitae. Curabitur scelerisque massa ac consequat luctus. In tincidunt blandit felis. In sed nulla diam. Nullam a auctor felis, ut pretium lacus.",
        onTextChanged = {},
        onBadgeClick = {},
        isTextOverLimit = false,
    )
}