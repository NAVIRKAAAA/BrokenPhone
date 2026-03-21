package com.brokentelephone.game.features.forgot_password.model

import androidx.compose.ui.text.input.TextFieldValue

data class ForgotPasswordState(
    val email: TextFieldValue = TextFieldValue(),
    val isLoading: Boolean = false,
    val globalError: String? = null,
    val isResetLinkSent: Boolean = false,
) {
    val isSendEnabled: Boolean
        get() = email.text.isNotBlank() && !isLoading
}
