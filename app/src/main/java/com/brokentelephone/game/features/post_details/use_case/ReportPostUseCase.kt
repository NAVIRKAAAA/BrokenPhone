package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.brokentelephone.game.domain.repository.ReportRepository
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.first

class ReportPostUseCase(
    private val repository: ReportRepository,
    private val userSession: UserSession,
) {

    suspend operator fun invoke(postId: String, type: ReportPostType) {
        val authState = userSession.authState.first()
        val user = authState.getUserOrNull() ?: return

        repository.report(user.id, postId, type)
    }

}
