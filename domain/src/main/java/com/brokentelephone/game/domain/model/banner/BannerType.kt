package com.brokentelephone.game.domain.model.banner

sealed class BannerType {
    data object ActiveSession : BannerType()
}
