package com.broken.telephone.features.dashboard.model

import com.broken.telephone.features.profile.model.UserUi

data class DashboardState(
    val posts: List<PostUi> = emptyList(),
    val user: UserUi? = null,
    val selectedPost: PostUi? = null,
    val isPostBottomSheetVisible: Boolean = false,
    val isReportBottomSheetVisible: Boolean = false,
    val isBlockDialogVisible: Boolean = false,
    val isBlockLoading: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isDeleteLoading: Boolean = false,
) {
    val isCurrentUserPost: Boolean get() = selectedPost?.authorId == user?.id
}
