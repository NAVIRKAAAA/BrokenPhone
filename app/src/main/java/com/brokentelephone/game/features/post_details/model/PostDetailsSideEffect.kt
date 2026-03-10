package com.brokentelephone.game.features.post_details.model

sealed interface PostDetailsSideEffect {
    data object ShowReportSuccessToast : PostDetailsSideEffect
    data class ShowCopyLinkSuccessToast(val link: String) : PostDetailsSideEffect
    data object NavigateBack : PostDetailsSideEffect
    data class NavigateToDraw(val postId: String) : PostDetailsSideEffect
    data class NavigateToDescribeDrawing(val postId: String) : PostDetailsSideEffect
}
