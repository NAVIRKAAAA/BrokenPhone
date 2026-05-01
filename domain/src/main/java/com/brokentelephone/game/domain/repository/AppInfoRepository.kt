package com.brokentelephone.game.domain.repository

interface AppInfoRepository {
    fun getVersionName(): String
    fun getVersionCode(): Int
}
