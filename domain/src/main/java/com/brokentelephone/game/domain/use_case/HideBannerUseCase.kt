package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.banner.BannerController

class HideBannerUseCase(
    private val bannerController: BannerController,
) {
    fun execute() = bannerController.hide()
}
