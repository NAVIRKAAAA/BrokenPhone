package com.brokentelephone.game.data.link

import com.brokentelephone.game.domain.link.LinkProvider

class MockLinkProviderImpl : LinkProvider {
    override fun getPostLinkById(postId: String): String = ""
    override fun getTermsOfServiceLink(): String = "https://google.com"
    override fun getPrivacyPolicyLink(): String = "https://google.com"
}
