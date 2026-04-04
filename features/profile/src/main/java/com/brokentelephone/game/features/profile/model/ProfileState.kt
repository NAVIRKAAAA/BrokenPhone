package com.brokentelephone.game.features.profile.model

import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.model.user.UserUi

data class ProfileState(
    val selectedTab: ProfileTab = ProfileTab.POSTS,
    val isAuth: Boolean = false,
    val user: UserUi? = null,
    val isPostsLoading: Boolean = true,
    val isInitialLoading: Boolean = true,
    val myPosts: List<PostUi> = emptyList(),
    val myContributions: List<PostUi> = emptyList(),
    val isContributionsLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    // Bottom sheets / dialogs
    val selectedPost: PostUi? = null,
    val isPostBottomSheetVisible: Boolean = false,
    val isBlockLoading: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isDeleteLoading: Boolean = false,
    val globalError: String? = null,
)
