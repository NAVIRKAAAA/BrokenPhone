package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.banner.BannerController
import com.brokentelephone.game.domain.model.banner.BannerType
import kotlinx.coroutines.flow.Flow

class ObserveBannerUseCase(
    private val bannerController: BannerController,
) {
    fun execute(): Flow<BannerType?> = bannerController.observe()
}
