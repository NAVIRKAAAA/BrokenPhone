package com.broken.telephone.domain.post

enum class PostStatus {
    AVAILABLE,      // Можна взяти і зробити mutation
    IN_PROGRESS,    // Хтось зараз працює
    COMPLETED       // Вже є наступна generation
}