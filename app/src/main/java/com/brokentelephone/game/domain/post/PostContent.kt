package com.brokentelephone.game.domain.post

sealed class PostContent {

    data class Text(
        val text: String,
    ) : PostContent()

    data class Drawing(
        val imageUrl: String? = null,
        val localPath: String? = null,
    ) : PostContent()

}
