package com.broken.telephone.domain.post

sealed class PostContent {
    data class Text(
        val text: String
    ) : PostContent()
    
    data class Drawing(
        val imageUrl: String? = null,  // URL малюнка в Storage (null поки не завантажено)
        val localPath: String? = null  // Локальний path якщо ще не uploaded
    ) : PostContent()
}