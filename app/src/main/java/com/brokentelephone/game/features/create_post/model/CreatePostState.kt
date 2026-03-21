package com.brokentelephone.game.features.create_post.model

import com.brokentelephone.game.features.profile.model.UserUi

data class CreatePostState(
    val text: String = "",
    val user: UserUi? = null,
    val generation: Int = 0,
    val maxGenerations: Int = 10,
    val textTimeLimit: Int = 60,
    val drawingTimeLimit: Int = 120,
    val showChainSettings: Boolean = false,
    val showStartNewChain: Boolean = false,
    val showDiscardDialog: Boolean = false,
    val isLoading: Boolean = false,
    val globalError: String? = null,
) {
    val isTextOverLimit: Boolean get() = text.length > MAX_TEXT_LENGTH

    companion object {
        const val MAX_TEXT_LENGTH = 140
    }
}