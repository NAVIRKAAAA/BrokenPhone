package com.brokentelephone.game.features.post_details.model

import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.domain.model.session.GameSession

data class PostDetailsState(
    val postUi: PostUi? = null,
    val userUi: UserUi? = null,
    val activeSession: GameSession? = null,
    val isBottomSheetVisible: Boolean = false,
    val isReportBottomSheetVisible: Boolean = false,
    val isBlockDialogVisible: Boolean = false,
    val isBlockLoading: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isDeleteLoading: Boolean = false,
    val isContinueLoading: Boolean = false,
    val globalError: String? = null,
    val globalException: Exception? = null,
    val isLoadRetrying: Boolean = false,
    val cooldownRemainingSeconds: Int = 0,
    val sessionRemainingSeconds: Int = 0,
) {
    val isOwnPost: Boolean get() =
        postUi != null && userUi != null && postUi.authorId == userUi.id

    val formattedCooldown: String get() {
        val minutes = cooldownRemainingSeconds / 60
        val seconds = cooldownRemainingSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    val formattedSessionTime: String get() =
        "%02d:%02d".format(sessionRemainingSeconds / 60, sessionRemainingSeconds % 60)

    val buttonType: PostDetailsButtonType get() {
        val post = postUi ?: return PostDetailsButtonType.DRAW
        return when {
            post.isCompleted -> PostDetailsButtonType.COMPLETED
            post.authorId == userUi?.id -> PostDetailsButtonType.OWN_POST
//            post.sessionsHistory.isMyActiveSession(userUi?.sessionId) -> PostDetailsButtonType.MY_SESSION
            post.status != PostStatus.AVAILABLE -> PostDetailsButtonType.UNAVAILABLE
            cooldownRemainingSeconds > 0 -> PostDetailsButtonType.COOLDOWN
            post.content is PostContent.Text -> PostDetailsButtonType.DRAW
            else -> PostDetailsButtonType.DESCRIBE
        }
    }
}
