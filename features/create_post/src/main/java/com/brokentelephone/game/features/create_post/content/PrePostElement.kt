package com.brokentelephone.game.features.create_post.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.avatar.AvatarComponent
import com.brokentelephone.game.core.composable.chip.PostChip
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.toShortFormattedTime
import com.brokentelephone.game.features.create_post.model.ChainSetting
import com.brokentelephone.game.features.create_post.model.CreatePostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrePostElement(
    name: String,
    text: String,
    onTextChanged: (String) -> Unit,
    onChainSettingClick: (ChainSetting) -> Unit,
    onDone: () -> Unit,
    isTextOverLimit: Boolean,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    avatarUrl: String? = null,
    maxGenerations: Int = 0,
    textTimeLimit: Int = 0,
    drawingTimeLimit: Int = 0,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {


            AvatarComponent(
                avatarUrl = avatarUrl,
                size = 40.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = name,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))

                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = onTextChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier),
                        textStyle = TextStyle(
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        minLines = 3,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                onDone()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    ) { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (text.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.create_post_placeholder),
                                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                            innerTextField()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {

                    PostChip(
                        text = stringResource(
                            R.string.create_post_badge_generations,
                            maxGenerations
                        ),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconResId = R.drawable.ic_mutations,
                        modifier = Modifier,
                        enabled = true,
                        onClick = { onChainSettingClick(ChainSetting.CHAIN_LENGTH) }
                    )

                    PostChip(
                        text = textTimeLimit.toShortFormattedTime(),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconResId = R.drawable.ic_edit_v2,
                        modifier = Modifier,
                        enabled = true,
                        onClick = { onChainSettingClick(ChainSetting.TEXT_TIME) }
                    )

                    PostChip(
                        text = drawingTimeLimit.toShortFormattedTime(),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconResId = R.drawable.ic_brush_v2,
                        modifier = Modifier,
                        enabled = true,
                        onClick = { onChainSettingClick(ChainSetting.DRAWING_TIME) }
                    )


                    if (text.isNotBlank()) {
                        val counterContentColor =
                            if (isTextOverLimit) MaterialTheme.colorScheme.onErrorContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        val counterContainerColor =
                            if (isTextOverLimit) MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.surfaceVariant

                        PostChip(
                            text = stringResource(
                                R.string.create_post_text_counter,
                                text.length,
                                CreatePostState.MAX_TEXT_LENGTH
                            ),
                            contentColor = counterContentColor,
                            containerColor = counterContainerColor,
                            iconResId = null,
                            modifier = Modifier,
                        )
                    }
                }
            }
        }

    }

}

@Preview
@Composable
fun PrePostElementPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            PrePostElement(
                name = "Alex",
                text = "",
//                text = "Дфдфдф",
//                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed lectus massa, gravida quis efficitur ut, vehicula id nulla. Phasellus placerat odio id tortor efficitur lacinia. Quisque a semper ante. In hac habitasse platea dictumst. Proin ut euismod massa. Sed sodales nibh purus, in consequat quam feugiat vitae. Curabitur scelerisque massa ac consequat luctus. In tincidunt blandit felis. In sed nulla diam. Nullam a auctor felis, ut pretium lacus.",
                onTextChanged = {},
                onChainSettingClick = {},
                isTextOverLimit = true,
                onDone = {},
                textTimeLimit = 60,
                drawingTimeLimit = 120,
                maxGenerations = 10
            )
        }
    }
}