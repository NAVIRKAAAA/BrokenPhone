package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.link.LinkProvider

class GetUserLinkByIdUseCase(
    private val linkProvider: LinkProvider,
) {
    fun execute(userId: String): String = linkProvider.getUserLinkById(userId)
}
