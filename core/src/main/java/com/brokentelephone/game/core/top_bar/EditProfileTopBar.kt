package com.brokentelephone.game.core.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun EditProfileTopBar(
    title: String,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    actions: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 18.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.primary,
        )

        if (actions != null) {
            actions()
        } else {
            Box(modifier = Modifier.size(48.dp))
        }
    }
}

@Preview
@Composable
private fun EditProfileTopBarPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            EditProfileTopBar(title = "Edit Profile")
        }
    }
}
