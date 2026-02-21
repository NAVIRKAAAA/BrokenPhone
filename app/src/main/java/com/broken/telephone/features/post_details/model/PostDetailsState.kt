package com.broken.telephone.features.post_details.model

import com.broken.telephone.features.dashboard.model.PostUi

data class PostDetailsState(
    val postUi: PostUi? = null,
    val isBottomSheetVisible: Boolean = false,
    val isReportBottomSheetVisible: Boolean = false,
    val isBlockDialogVisible: Boolean = false,
    val isBlockLoading: Boolean = false,
)
