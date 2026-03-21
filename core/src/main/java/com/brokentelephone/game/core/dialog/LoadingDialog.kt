package com.brokentelephone.game.core.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(96.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
            )
        }
    }
}

@Preview
@Composable
private fun LoadingDialogPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingDialog()
        }
    }
}
