package com.brokentelephone.game.features.confirm_sign_up.model

const val OTP_CODE_LENGTH = 8

data class ConfirmSignUpState(
    val email: String = "",
    val code: String = "",
    val isConfirmLoading: Boolean = false,
    val isResendLoading: Boolean = false,
    val resendCooldownSeconds: Int = 0,
    val globalError: String? = null,
) {
    val isConfirmEnabled: Boolean get() = code.length == OTP_CODE_LENGTH && !isConfirmLoading
    val isResendEnabled: Boolean get() = resendCooldownSeconds == 0

    val formattedCooldown: String get() {
        val minutes = resendCooldownSeconds / 60
        val seconds = resendCooldownSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}
