package com.brokentelephone.game.features.post_details.model

import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.profile.model.UserUi

data class PostDetailsState(
    val postUi: PostUi? = null,
    val userUi: UserUi? = null,
    val isBottomSheetVisible: Boolean = false,
    val isReportBottomSheetVisible: Boolean = false,
    val isBlockDialogVisible: Boolean = false,
    val isBlockLoading: Boolean = false,
    val isContinueLoading: Boolean = false,
    val globalError: String? = null,
    val globalException: Exception? = null,
    val isLoadRetrying: Boolean = false,
)
