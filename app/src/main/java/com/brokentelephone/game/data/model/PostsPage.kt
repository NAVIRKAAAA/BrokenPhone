package com.brokentelephone.game.data.model

import com.brokentelephone.game.domain.post.Post
import com.google.firebase.firestore.DocumentSnapshot

data class PostsPage(
    val posts: List<Post>,
    val lastDocRef: DocumentSnapshot?,
)