package com.brokentelephone.game.features.draw.model

sealed interface DrawSideEffect {
    data class PostCreated(val localPath: String) : DrawSideEffect
    data object NavigateBack : DrawSideEffect
}
