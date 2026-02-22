package com.broken.telephone.data.repository

import com.broken.telephone.domain.repository.AppInfoRepository

class MockAppInfoRepositoryImpl : AppInfoRepository {
    override fun getVersionName(): String = "1.0.0"
    override fun getVersionCode(): Int = 1
}
