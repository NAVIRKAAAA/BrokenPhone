package com.broken.telephone.domain.post

sealed class PostContent {
    data class Text(
        val text: String
    ) : PostContent()
    
    data class Drawing(
        val imageUrl: String,          // URL малюнка в Storage
        val localPath: String? = null  // Локальний path якщо ще не uploaded
    ) : PostContent()
}