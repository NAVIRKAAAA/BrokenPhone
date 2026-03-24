package com.brokentelephone.game.features.chain_details.model

import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.features.dashboard.model.PostUi

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
