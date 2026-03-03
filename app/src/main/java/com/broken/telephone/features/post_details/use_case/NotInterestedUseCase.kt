package com.broken.telephone.features.post_details.use_case

import com.broken.telephone.domain.repository.NotInterestedRepository
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.flow.first

class NotInterestedUseCase(
    private val repository: NotInterestedRepository,
    private val userSession: UserSession,
) {

    suspend operator fun invoke(postParentId: String) {
        val authState = userSession.authState.first()
        val user = authState.getUserOrNull() ?: return

        repository.notInterested(user.id, postParentId)
    }

}
