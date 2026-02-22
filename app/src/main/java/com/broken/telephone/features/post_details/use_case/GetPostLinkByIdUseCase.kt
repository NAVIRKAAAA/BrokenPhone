package com.broken.telephone.features.post_details.use_case

import com.broken.telephone.domain.link.LinkProvider

class GetPostLinkByIdUseCase(
    private val linkProvider: LinkProvider,
) {
    operator fun invoke(postId: String): String = linkProvider.getPostLinkById(postId)
}
