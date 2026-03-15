package com.brokentelephone.game.features.dashboard.model

import com.brokentelephone.game.features.profile.model.UserUi

data class DashboardState(
    val posts: List<PostUi> = emptyList(),
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val user: UserUi? = null,
    val selectedSort: DashboardSort = DashboardSort.LATEST,
    val selectedPost: PostUi? = null,
    val isPostBottomSheetVisible: Boolean = false,
    val isReportBottomSheetVisible: Boolean = false,
    val isBlockDialogVisible: Boolean = false,
    val isBlockLoading: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isDeleteLoading: Boolean = false,
    val globalError: String? = null,
) {
    val isCurrentUserPost: Boolean get() = selectedPost?.authorId == user?.id
}
