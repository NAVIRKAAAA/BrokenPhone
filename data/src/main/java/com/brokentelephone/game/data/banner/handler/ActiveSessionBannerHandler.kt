package com.brokentelephone.game.data.banner.handler

import com.brokentelephone.game.domain.banner.BannerHandler
import com.brokentelephone.game.domain.model.banner.BannerType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ActiveSessionBannerHandler : BannerHandler<BannerType.ActiveSession> {

    override fun handle(data: BannerType.ActiveSession): Flow<BannerType> = flow {
        while (true) {
            val remaining = ((data.expiresAt - System.currentTimeMillis()) / 1000).toInt()
            emit(data.copy(remainingSeconds = remaining.coerceAtLeast(0)))
            if (remaining <= 0) break
            delay(1000)
        }
    }
}
