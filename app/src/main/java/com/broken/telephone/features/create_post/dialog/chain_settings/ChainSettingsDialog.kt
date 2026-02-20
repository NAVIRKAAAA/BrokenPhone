package com.broken.telephone.features.create_post.dialog.chain_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme

private const val CHAIN_LENGTH_MIN = 6
private const val CHAIN_LENGTH_MAX = 20
private const val CHAIN_LENGTH_STEP = 2
private const val TEXT_TIME_MIN = 15
private const val TEXT_TIME_MAX = 60
private const val TEXT_TIME_STEP = 5
private const val DRAWING_TIME_MIN = 30
private const val DRAWING_TIME_MAX = 120
private const val DRAWING_TIME_STEP = 5

@Composable
fun ChainSettingsDialog(
    initialChainLength: Int,
    initialTextTimeLimit: Int,
    initialDrawingTimeLimit: Int,
    onDismiss: () -> Unit,
    onConfirm: (chainLength: Int, textTimeLimit: Int, drawingTimeLimit: Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    var chainLength by remember { mutableIntStateOf(initialChainLength) }
    var textTimeLimit by remember { mutableIntStateOf(initialTextTimeLimit) }
    var drawingTimeLimit by remember { mutableIntStateOf(initialDrawingTimeLimit) }
    val hasChanged = chainLength != initialChainLength
            || textTimeLimit != initialTextTimeLimit
            || drawingTimeLimit != initialDrawingTimeLimit

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chain settings",
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 18.sp,
                    lineHeight = 28.sp
                )

                IconButton(
                    onClick = onDismiss
                ) {

                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null,
                    )

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ChainSettingsItem(
                title = "Chain length",
                value = chainLength.toString(),
                range = "$CHAIN_LENGTH_MIN-$CHAIN_LENGTH_MAX",
                onMinusClick = { chainLength -= CHAIN_LENGTH_STEP },
                onPlusClick = { chainLength += CHAIN_LENGTH_STEP },
                isMinusEnabled = chainLength > CHAIN_LENGTH_MIN,
                isPlusEnabled = chainLength < CHAIN_LENGTH_MAX,
            )

            Spacer(modifier = Modifier.height(20.dp))

            ChainSettingsItem(
                title = "Text response",
                value = "${textTimeLimit}s",
                range = "${TEXT_TIME_MIN}s-${TEXT_TIME_MAX}s",
                onMinusClick = { textTimeLimit -= TEXT_TIME_STEP },
                onPlusClick = { textTimeLimit += TEXT_TIME_STEP },
                isMinusEnabled = textTimeLimit > TEXT_TIME_MIN,
                isPlusEnabled = textTimeLimit < TEXT_TIME_MAX,
            )

            Spacer(modifier = Modifier.height(20.dp))

            ChainSettingsItem(
                title = "Drawing response",
                value = "${drawingTimeLimit}s",
                range = "${DRAWING_TIME_MIN}s-${DRAWING_TIME_MAX}s",
                onMinusClick = { drawingTimeLimit -= DRAWING_TIME_STEP },
                onPlusClick = { drawingTimeLimit += DRAWING_TIME_STEP },
                isMinusEnabled = drawingTimeLimit > DRAWING_TIME_MIN,
                isPlusEnabled = drawingTimeLimit < DRAWING_TIME_MAX,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onConfirm(chainLength, textTimeLimit, drawingTimeLimit) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = hasChanged,
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(
                    text = "Done",
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 18.sp,
                    lineHeight = 28.sp
                )
            }

        }

    }

}


@Preview
@Composable
fun ChainSettingsDialogPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ChainSettingsDialog(
                initialChainLength = 10,
                initialTextTimeLimit = 30,
                initialDrawingTimeLimit = 60,
                onDismiss = {},
                onConfirm = { _, _, _ -> },
            )
        }
    }
}