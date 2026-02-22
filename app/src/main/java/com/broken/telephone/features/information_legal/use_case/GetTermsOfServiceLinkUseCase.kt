package com.broken.telephone.features.information_legal.use_case

import com.broken.telephone.domain.link.LinkProvider

class GetTermsOfServiceLinkUseCase(
    private val linkProvider: LinkProvider,
) {
    operator fun invoke(): String = linkProvider.getTermsOfServiceLink()
}
