package com.broken.telephone.features.create_post.dialog.start_new_chain

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                text = "Start new chain?",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 18.sp,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your post will start a chain that others can continue. This cannot be undone.",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(12.dp))

            StartNewChainSettingsItem(
                text = "Chain length: $maxGenerations posts",
                iconResId = R.drawable.ic_mutations,
            )

            Spacer(modifier = Modifier.height(8.dp))

            StartNewChainSettingsItem(
                text = "Text time: ${textTimeLimit}s",
                iconResId = R.drawable.ic_clock,
            )

            Spacer(modifier = Modifier.height(8.dp))

            StartNewChainSettingsItem(
                text = "Draw time: ${drawingTimeLimit}s",
                iconResId = R.drawable.ic_clock,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Settings apply to the entire chain",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Color(0xFF666666)
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
                        .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    enabled = true,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = "Cancel",
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
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
                        contentColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Start Chain",
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
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
    BrokenTelephoneTheme {
        StartNewChainDialog(
            maxGenerations = 10,
            textTimeLimit = 30,
            drawingTimeLimit = 60,
            isLoading = true,
            onDismiss = {},
            onCancel = {},
            onStartChain = {},
        )
    }
}
