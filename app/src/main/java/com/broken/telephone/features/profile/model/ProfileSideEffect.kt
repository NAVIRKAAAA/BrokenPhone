package com.broken.telephone.features.profile.model

sealed interface ProfileSideEffect {
    data object ShowReportSuccessToast : ProfileSideEffect
    data object ShowNotInterestedToast : ProfileSideEffect
    data class ShowCopyLinkSuccessToast(val link: String) : ProfileSideEffect
}
