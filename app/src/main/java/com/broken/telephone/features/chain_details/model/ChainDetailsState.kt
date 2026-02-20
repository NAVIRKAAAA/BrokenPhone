package com.broken.telephone.features.chain_details.model

import com.broken.telephone.domain.post.PostChainEntry
import com.broken.telephone.features.dashboard.model.PostUi

data class ChainDetailsState(
    val postId: String = "",
    val chains: List<PostUi> = listOf()
)
