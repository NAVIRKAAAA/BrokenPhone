package com.brokentelephone.game.features.sign_up.model

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val globalError: String? = null,
    val isLoading: Boolean = false,
) {
    val isSignUpEnabled: Boolean
        get() = email.isNotBlank()
            && password.isNotBlank()
            && confirmPassword.isNotBlank()
            && !isLoading
}
