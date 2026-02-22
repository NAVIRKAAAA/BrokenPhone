package com.broken.telephone.domain.repository

interface AppInfoRepository {
    fun getVersionName(): String
    fun getVersionCode(): Int
}
