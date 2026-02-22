package com.broken.telephone.domain.link

interface LinkProvider {
    fun getPostLinkById(postId: String): String
    fun getTermsOfServiceLink(): String
    fun getPrivacyPolicyLink(): String
}