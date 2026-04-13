package com.brokentelephone.game.features.create_post.model

import com.brokentelephone.game.core.model.user.UserUi

data class CreatePostState(
    val text: String = "",
    val user: UserUi? = null,
    val generation: Int = 0,
    val maxGenerations: Int = DEFAULT_MAX_GENERATIONS,
    val textTimeLimit: Int = DEFAULT_TEXT_TIME_LIMIT,
    val drawingTimeLimit: Int = DEFAULT_DRAWING_TIME_LIMIT,
    val activeChainSetting: ChainSetting? = null,
    val showStartNewChain: Boolean = false,
    val showDiscardDialog: Boolean = false,
    val isLoading: Boolean = false,
    val globalError: String? = null,
) {
    val isTextOverLimit: Boolean get() = text.length > MAX_TEXT_LENGTH

    companion object {
        const val MAX_TEXT_LENGTH = 140
        const val DEFAULT_MAX_GENERATIONS = 10
        const val DEFAULT_TEXT_TIME_LIMIT = 60
        const val DEFAULT_DRAWING_TIME_LIMIT = 120
    }
}