package com.brokentelephone.game.features.settings.use_case

import com.brokentelephone.game.domain.link.LinkProvider

class GetPrivacyPolicyLinkUseCase(
    private val linkProvider: LinkProvider,
) {
    operator fun invoke(): String = linkProvider.getPrivacyPolicyLink()
}
