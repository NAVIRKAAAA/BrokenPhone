package com.broken.telephone.navigation.routes

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Welcome : Routes()

    @Serializable
    data object Dashboard : Routes()

    @Serializable
    data object CreatePost : Routes()
}