package com.brokentelephone.game.domain.model.post

enum class PostStatus {
    AVAILABLE,
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun fromString(value: String?): PostStatus =
            entries.find { it.name == value } ?: AVAILABLE
    }
}