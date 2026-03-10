package com.brokentelephone.game.features.chain_details.model

import com.brokentelephone.game.features.dashboard.model.PostUi

data class ChainDetailsState(
    val postId: String = "",
    val post: PostUi? = null,
    val chains: List<PostUi> = listOf(),
    val currentUserId: String? = null
)
