package com.broken.telephone.features.profile.model

import com.broken.telephone.features.dashboard.model.PostUi

data class ProfileState(
    val selectedTab: ProfileTab = ProfileTab.POSTS,
    val isAuth: Boolean = false,
    val user: UserUi? = null,
    val myPosts: List<PostUi> = emptyList(),
    val myContributions: List<PostUi> = emptyList(),
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
