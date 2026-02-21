package com.broken.telephone.features.post_details.model

sealed interface PostDetailsSideEffect {
    data object ShowReportSuccessToast : PostDetailsSideEffect
    data object NavigateBack : PostDetailsSideEffect
}
