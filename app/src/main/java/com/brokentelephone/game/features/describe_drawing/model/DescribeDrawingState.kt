package com.brokentelephone.game.features.describe_drawing.model

import com.brokentelephone.game.features.dashboard.model.PostUi

data class DescribeDrawingState(
    val postUi: PostUi? = null,
    val text: String = "",
    val showDiscardDialog: Boolean = false,
    val isCancelling: Boolean = false,
    val showPostConfirmDialog: Boolean = false,
    val isPosting: Boolean = false,
    val remainingSeconds: Int = 0,
    val isTimerExpired: Boolean = false,
    val showTimesUpDialog: Boolean = false,
    val globalError: String? = null,
) {
    val isTextOverLimit: Boolean get() = text.length > MAX_TEXT_LENGTH
    val formattedTime: String get() {
        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    companion object {
        const val MAX_TEXT_LENGTH = 140
    }
}
