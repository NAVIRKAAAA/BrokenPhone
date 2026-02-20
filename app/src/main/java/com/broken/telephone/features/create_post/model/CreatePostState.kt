package com.broken.telephone.features.create_post.model

data class CreatePostState(
    val text: String = "",
    val avatarUrl: String = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_23.png",
    val generation: Int = 0,               // Поточна позиція (0, 1, 2...)
    val maxGenerations: Int = 10,          // Максимальна довжина (5-20)
    val textTimeLimit: Int = 30,           // Час на текст (секунди)
    val drawingTimeLimit: Int = 60,        // Час на малюнок (секунди)
    val showChainSettings: Boolean = false,
    val showStartNewChain: Boolean = false,
    val isLoading: Boolean = false,
) {
    val isTextOverLimit: Boolean get() = text.length > MAX_TEXT_LENGTH

    companion object {
        const val MAX_TEXT_LENGTH = 140
    }
}