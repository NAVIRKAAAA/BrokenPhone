package com.brokentelephone.game.domain.model.notification

data class Notification(
    val id: String,
    val receiversIds: List<String>,
    val data: NotificationData,
    val createdAt: Long,
)
