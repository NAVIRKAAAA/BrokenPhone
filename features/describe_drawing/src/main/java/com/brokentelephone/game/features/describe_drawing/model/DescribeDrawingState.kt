package com.brokentelephone.game.features.describe_drawing.model

import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.domain.model.session.GameSession

data class DescribeDrawingState(
    val postUi: PostUi? = null,
    val session: GameSession? = null,
    val text: String = "",
    val showDiscardDialog: Boolean = false,
    val isCancelling: Boolean = false,
    val isPosting: Boolean = false,
    val remainingSeconds: Int = 0,
    val isTimerExpired: Boolean = false,
    val showTimesUpDialog: Boolean = false,
    val globalError: String? = null,
) {
    val isTextOverLimit: Boolean get() = text.length > MAX_TEXT_LENGTH

    companion object {
        const val MAX_TEXT_LENGTH = 140
    }
}
