package com.brokentelephone.game.features.settings.use_case

import com.brokentelephone.game.domain.link.LinkProvider

class GetTermsOfServiceLinkUseCase(
    private val linkProvider: LinkProvider,
) {
    operator fun invoke(): String = linkProvider.getTermsOfServiceLink()
}
