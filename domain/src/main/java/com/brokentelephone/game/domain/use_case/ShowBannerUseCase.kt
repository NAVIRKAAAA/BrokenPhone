package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.banner.BannerController
import com.brokentelephone.game.domain.model.banner.BannerType

class ShowBannerUseCase(
    private val bannerController: BannerController,
) {
    fun execute(banner: BannerType) = bannerController.show(banner)
}
