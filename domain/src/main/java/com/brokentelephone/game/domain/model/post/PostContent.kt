package com.brokentelephone.game.domain.model.post

sealed class PostContent {

    data class Text(
        val text: String,
    ) : PostContent()

    data class Drawing(
        val imageUrl: String? = null,
        val localPath: String? = null,
    ) : PostContent()

}
