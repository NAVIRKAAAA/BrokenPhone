package com.broken.telephone.features.information_legal.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.edit_profile.content.EditProfileTopBar
import com.broken.telephone.features.settings.content.SettingsItem

@Composable
fun InformationLegalContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onTermsOfServiceClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = "Information & Legal",
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsItem(
            text = "Terms of Service",
            onClick = onTermsOfServiceClick,
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        SettingsItem(
            text = "Privacy Policy",
            onClick = onPrivacyPolicyClick,
        )

        Spacer(modifier = Modifier.weight(1f).navigationBarsPadding())
    }
}

@Preview
@Composable
fun InformationLegalContentPreview() {
    BrokenTelephoneTheme {
        InformationLegalContent()
    }
}
