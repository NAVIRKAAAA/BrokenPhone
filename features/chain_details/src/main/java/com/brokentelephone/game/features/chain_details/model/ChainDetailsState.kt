package com.brokentelephone.game.features.chain_details.model

import com.brokentelephone.game.core.model.post.PostUi

data class ChainDetailsState(
    val postId: String = "",
    val userId: String = "",
    val post: PostUi? = null,
    val chain: List<PostUi> = listOf(),
    val isLoading: Boolean = true,
    val globalError: String? = null,
    val globalException: Throwable? = null,
    val isRefreshing: Boolean = false
)
