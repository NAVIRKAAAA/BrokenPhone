package com.brokentelephone.game.core.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun ErrorDialog(
    body: String,
    modifier: Modifier = Modifier,
    onOkClick: () -> Unit = {},
) {
    Dialog(onDismissRequest = {}) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.error_dialog_title),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 18.sp,
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = body,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onOkClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(
                    text = stringResource(R.string.error_dialog_ok),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 18.sp,
                    lineHeight = 28.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun ErrorDialogPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            ErrorDialog(body = "We couldn't create your account. Please try again later.")
        }
    }
}
