package com.broken.telephone.domain.link

interface LinkProvider {
    fun getPostLinkById(postId: String): String
}