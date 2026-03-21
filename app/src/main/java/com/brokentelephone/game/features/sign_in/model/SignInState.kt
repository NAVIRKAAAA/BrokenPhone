package com.brokentelephone.game.features.sign_in.model

import androidx.compose.ui.text.input.TextFieldValue

data class SignInState(
    val email: TextFieldValue = TextFieldValue(),
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val credentialsError: String? = null,
    val globalError: String? = null,
) {
    val isSignInEnabled: Boolean
        get() = email.text.isNotBlank() && password.isNotBlank() && !isLoading
}
