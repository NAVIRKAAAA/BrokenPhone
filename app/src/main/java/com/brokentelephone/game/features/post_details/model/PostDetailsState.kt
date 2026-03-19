package com.brokentelephone.game.features.post_details.model

import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.profile.model.UserUi

data class PostDetailsState(
    val postUi: PostUi? = null,
    val userUi: UserUi? = null,
    val isBottomSheetVisible: Boolean = false,
    val isReportBottomSheetVisible: Boolean = false,
    val isBlockDialogVisible: Boolean = false,
    val isBlockLoading: Boolean = false,
    val isContinueLoading: Boolean = false,
    val globalError: String? = null,
    val globalException: Exception? = null,
    val isLoadRetrying: Boolean = false,
    val cooldownRemainingSeconds: Int = 0,
) {
    val formattedCooldown: String get() {
        val minutes = cooldownRemainingSeconds / 60
        val seconds = cooldownRemainingSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    val buttonType: PostDetailsButtonType get() {
        val post = postUi ?: return PostDetailsButtonType.DRAW
        return when {
            post.isCompleted -> PostDetailsButtonType.COMPLETED
            post.status != PostStatus.AVAILABLE -> PostDetailsButtonType.UNAVAILABLE
            cooldownRemainingSeconds > 0 -> PostDetailsButtonType.COOLDOWN
            post.content is PostContent.Text -> PostDetailsButtonType.DRAW
            else -> PostDetailsButtonType.DESCRIBE
        }
    }
}
