package com.broken.telephone.core.dialog

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.broken.telephone.R

@Composable
fun ConfirmDialog(
    title: String,
    body: String,
    cancelText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    confirmButtonColor: Color = MaterialTheme.colorScheme.error,
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
                text = title,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 18.sp,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = body,
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
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = cancelText,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }

                Button(
                    onClick = onConfirm,
                    enabled = !isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = confirmButtonColor
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = confirmText,
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
