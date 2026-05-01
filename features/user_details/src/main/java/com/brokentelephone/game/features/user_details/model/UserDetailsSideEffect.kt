package com.brokentelephone.game.features.user_details.model

sealed class UserDetailsSideEffect {
    data object NavigateBack : UserDetailsSideEffect()
    data object NavigateBackWithForceUpdate : UserDetailsSideEffect()
    data object ShowReportSuccessToast : UserDetailsSideEffect()
    data class ShowCopyLinkSuccessToast(val link: String) : UserDetailsSideEffect()
}
