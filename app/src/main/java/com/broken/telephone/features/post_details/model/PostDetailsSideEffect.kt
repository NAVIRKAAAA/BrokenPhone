package com.broken.telephone.features.post_details.model

sealed interface PostDetailsSideEffect {
    data object ShowReportSuccessToast : PostDetailsSideEffect
    data class ShowCopyLinkSuccessToast(val link: String) : PostDetailsSideEffect
    data object NavigateBack : PostDetailsSideEffect
}
