package com.broken.telephone.features.post_details.use_case

import com.broken.telephone.domain.repository.BlockRepository
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.flow.first

class BlockUserUseCase(
    private val repository: BlockRepository,
    private val userSession: UserSession,
) {

    suspend operator fun invoke(blockedUserId: String) {
        val authState = userSession.authState.first()
        if (authState !is AuthState.Auth) return
        repository.block(authState.user.id, blockedUserId)
    }

}
