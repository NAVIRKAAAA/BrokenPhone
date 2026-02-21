package com.broken.telephone.features.post_details.use_case

import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.broken.telephone.domain.repository.ReportRepository
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.flow.first

class ReportPostUseCase(
    private val repository: ReportRepository,
    private val userSession: UserSession,
) {

    suspend operator fun invoke(postId: String, type: ReportPostType) {
        val authState = userSession.authState.first()
        if (authState !is AuthState.Auth) return
        repository.report(authState.user.id, postId, type)
    }

}
