package com.brokentelephone.game.domain.model.banner

sealed class BannerType {

    data class ActiveSession(
        val id: String,
        val postId: String,
        val expiresAt: Long,
        val remainingSeconds: Int,
        val totalSeconds: Int,
    ) : BannerType() {
        val formattedTime: String
            get() = "%02d:%02d".format(remainingSeconds / 60, remainingSeconds % 60)

        val progress: Float
            get() = (remainingSeconds / totalSeconds.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    }

    data class NewsNotification(
        val id: String,
        val title: String,
        val body: String,
        val expiresAt: Long,
        val remainingSeconds: Int,
        val totalSeconds: Int,
    ) : BannerType() {
        val progress: Float
            get() = (remainingSeconds / totalSeconds.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    }

}
