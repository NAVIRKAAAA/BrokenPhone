package com.brokentelephone.game.domain.model.pagination

import com.brokentelephone.game.domain.model.post.Post

data class PostsPage(
    val posts: List<Post>,
    val offset: Int,
    val hasMore: Boolean,
)
