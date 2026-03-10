package com.brokentelephone.game.features.post_details.model

import com.brokentelephone.game.features.dashboard.model.PostUi

data class PostDetailsState(
    val postUi: PostUi? = null,
    val isCurrentUserPost: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val isReportBottomSheetVisible: Boolean = false,
    val isBlockDialogVisible: Boolean = false,
    val isBlockLoading: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isDeleteLoading: Boolean = false,
    val isContinueLoading: Boolean = false,
)
