package com.brokentelephone.game.features.new_password.model

data class NewPasswordState(
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val globalError: String? = null,
    val isLoading: Boolean = false,
) {
    val isSaveEnabled: Boolean
        get() = password.isNotBlank() && confirmPassword.isNotBlank() && !isLoading
}
