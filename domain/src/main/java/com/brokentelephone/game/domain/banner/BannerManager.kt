package com.brokentelephone.game.domain.banner

import com.brokentelephone.game.domain.model.banner.BannerType
import kotlinx.coroutines.flow.Flow

// TODO: Need Review
interface BannerManager {
    val banners: Flow<BannerType>
    fun show(banner: BannerType)
}
