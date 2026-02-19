package com.broken.telephone.domain.post

sealed class PostContent {
    abstract val timeLimit: Int

    data class Text(
        val text: String,
        override val timeLimit: Int,
    ) : PostContent()

    data class Drawing(
        override val timeLimit: Int,
        val imageUrl: String? = null,
        val localPath: String? = null,
    ) : PostContent()
}
