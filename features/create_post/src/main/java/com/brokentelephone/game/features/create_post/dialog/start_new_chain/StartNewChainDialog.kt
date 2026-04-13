package com.brokentelephone.game.features.create_post.dialog.start_new_chain

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun StartNewChainDialog(
    maxGenerations: Int,
    textTimeLimit: Int,
    drawingTimeLimit: Int,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onStartChain: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {

            Text(
                text = stringResource(R.string.create_post_start_chain_title),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 18.sp,
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.create_post_start_chain_body),
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            StartNewChainSettingsItem(
                text = stringResource(R.string.create_post_start_chain_chain_length, maxGenerations),
                iconResId = R.drawable.ic_mutations,
            )

            Spacer(modifier = Modifier.height(8.dp))

            StartNewChainSettingsItem(
                text = stringResource(R.string.create_post_start_chain_text_time, textTimeLimit),
                iconResId = R.drawable.ic_edit_v2,
            )

            Spacer(modifier = Modifier.height(8.dp))

            StartNewChainSettingsItem(
                text = stringResource(R.string.create_post_start_chain_draw_time, drawingTimeLimit),
                iconResId = R.drawable.ic_brush_v2,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.create_post_start_chain_settings_note),
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = Color.Transparent
                    ),
                    enabled = true,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = stringResource(R.string.common_cancel),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }

                Button(
                    onClick = onStartChain,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = LocalContentColor.current,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.create_post_start_chain_confirm),
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }

    }

}

@Preview()
@Composable
fun StartNewChainDialogPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            StartNewChainDialog(
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                isLoading = false,
                onDismiss = {},
                onCancel = {},
                onStartChain = {},
            )
        }
    }
}
