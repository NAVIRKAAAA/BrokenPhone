package com.brokentelephone.game.features.chain_details.model

import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.profile.model.UserUi

data class ChainDetailsState(
    val postId: String = "",
    val post: PostUi? = null,
    val chain: List<PostUi> = listOf(),
    val isLoading: Boolean = true,
    val userUi: UserUi? = null,
    val globalError: String? = null,
    val globalException: Throwable? = null,
    val isRefreshing: Boolean = false
)
