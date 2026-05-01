package com.brokentelephone.game.domain.banner

import com.brokentelephone.game.domain.model.banner.BannerType
import kotlinx.coroutines.flow.Flow

interface BannerController {
    fun observe(): Flow<BannerType?>
    fun show(banner: BannerType)
    fun hide()
}
