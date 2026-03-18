package com.brokentelephone.game.domain.link

interface LinkProvider {
    fun getPostLinkById(postId: String): String
    fun getTermsOfServiceLink(): String
    fun getPrivacyPolicyLink(): String
}