package com.brokentelephone.game.features.edit_email.model

import androidx.compose.ui.text.input.TextFieldValue

data class EditEmailState(
    val email: TextFieldValue = TextFieldValue(),
    val initialEmail: String = "",
    val isLoading: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val globalError: String? = null,
    val emailError: String? = null,
) {
    val isSaveEnabled: Boolean
        get() = email.text.isNotBlank()
            && email.text != initialEmail
            && !isLoading
}
