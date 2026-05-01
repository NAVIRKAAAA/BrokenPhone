package com.brokentelephone.game.features.post_details.model

sealed interface PostDetailsSideEffect {
    data object ShowReportSuccessToast : PostDetailsSideEffect
    data class ShowCopyLinkSuccessToast(val link: String) : PostDetailsSideEffect
    data object NavigateBack : PostDetailsSideEffect
    data object NavigateBackWithForceUpdate : PostDetailsSideEffect
    data class NavigateToDraw(val sessionId: String) : PostDetailsSideEffect
    data class NavigateToDescribeDrawing(val sessionId: String) : PostDetailsSideEffect
}
