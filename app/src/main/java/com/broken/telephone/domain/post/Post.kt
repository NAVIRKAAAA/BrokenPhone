package com.broken.telephone.domain.post

data class Post(
    val id: String,                    // Unique ID поста
    val authorId: String,              // ID автора
    val authorName: String,            // Ім'я автора
    val avatarUrl: String?,            // URL аватара (null якщо немає)
    val content: PostContent,          // Текст або малюнок
    val createdAt: Long,               // Timestamp створення
    val status: PostStatus,            // Статус (available, in_progress, completed)

    // Chain info
    val generation: Int,               // Поточна позиція (0, 1, 2...)
    val maxGenerations: Int,           // Максимальна довжина (5-20)

    // Time limits
    val textTimeLimit: Int,            // Час на текст (секунди)
    val drawingTimeLimit: Int,         // Час на малюнок (секунди)

    // Locking
    val lockedBy: String?              // ID користувача що працює (null якщо вільно)
)