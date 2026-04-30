package com.brokentelephone.game.domain.banner

import com.brokentelephone.game.domain.model.banner.BannerType
import kotlinx.coroutines.flow.Flow

interface BannerHandler<in T> {
    fun handle(data: T): Flow<BannerType>
}
