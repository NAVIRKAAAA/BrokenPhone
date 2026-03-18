package com.brokentelephone.game.domain.model.pagination

import com.brokentelephone.game.domain.model.post.Post
import com.google.firebase.firestore.DocumentSnapshot

data class PostsPage(
    val posts: List<Post>,
    val lastDocRef: DocumentSnapshot?,
    val hasMore: Boolean
)