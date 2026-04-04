package com.brokentelephone.game.data.link

import com.brokentelephone.game.domain.link.LinkProvider

class LinkProviderImpl : LinkProvider {
    override fun getPostLinkById(postId: String): String = ""
    override fun getUserLinkById(userId: String): String = ""
    override fun getTermsOfServiceLink(): String = "https://google.com"
    override fun getPrivacyPolicyLink(): String = "https://google.com"
}
