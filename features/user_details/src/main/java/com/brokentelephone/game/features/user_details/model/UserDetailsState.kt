package com.brokentelephone.game.features.user_details.model

import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.domain.model.friend.FriendshipActionState

data class UserDetailsState(
    val isUserLoading: Boolean = true,
    val friendshipActionState: FriendshipActionState? = null,
    val isFriendshipActionLoading: Boolean = false,
    val user: UserUi? = null,
    val globalError: String? = null,
    val selectedTab: ProfileTab = ProfileTab.POSTS,
    val isPostsLoading: Boolean = true,
    val isInitialLoading: Boolean = true,
    val myPosts: List<PostUi> = emptyList(),
    val myContributions: List<PostUi> = emptyList(),
    val isContributionsLoading: Boolean = true,
    val isRefreshing: Boolean = false,
)
