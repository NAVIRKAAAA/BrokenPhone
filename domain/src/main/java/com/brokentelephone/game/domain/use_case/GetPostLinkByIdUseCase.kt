package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.link.LinkProvider

class GetPostLinkByIdUseCase(
    private val linkProvider: LinkProvider,
) {
    operator fun invoke(postId: String): String = linkProvider.getPostLinkById(postId)
}
