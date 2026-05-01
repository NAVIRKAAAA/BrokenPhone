package com.brokentelephone.game.features.forgot_password.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.forgot_password.R
import com.brokentelephone.game.core.R as CoreR

@Composable
fun PasswordResetSentDialog(
    modifier: Modifier = Modifier,
    onGotItClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = { return@Dialog },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(CoreR.drawable.ic_complete),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.forgot_password_reset_sent_title),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(CoreR.font.nunito_bold)),
                fontSize = 18.sp,
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.forgot_password_reset_sent_body),
                fontFamily = FontFamily(Font(CoreR.font.nunito_regular)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onGotItClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues()
            ) {
                Text(
                    text = stringResource(R.string.forgot_password_reset_sent_got_it),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(CoreR.font.nunito_bold)),
                    fontSize = 18.sp,
                    lineHeight = 28.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PasswordResetSentDialogPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            PasswordResetSentDialog()
        }
    }
}
