package com.brokentelephone.game.data.repository

import com.brokentelephone.game.domain.repository.AppInfoRepository

class MockAppInfoRepositoryImpl : AppInfoRepository {
    override fun getVersionName(): String = "1.0.0"
    override fun getVersionCode(): Int = 1
}
