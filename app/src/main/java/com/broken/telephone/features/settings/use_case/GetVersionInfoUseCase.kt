package com.broken.telephone.features.settings.use_case

import com.broken.telephone.domain.repository.AppInfoRepository

class GetVersionInfoUseCase(
    private val repository: AppInfoRepository,
) {
    operator fun invoke(): String {
        val versionName = repository.getVersionName()
        val versionCode = repository.getVersionCode()
        return "$versionName ($versionCode)"
    }
}
