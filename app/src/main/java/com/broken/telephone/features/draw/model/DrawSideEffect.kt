package com.broken.telephone.features.draw.model

sealed interface DrawSideEffect {
    data class PostCreated(val localPath: String) : DrawSideEffect
}
