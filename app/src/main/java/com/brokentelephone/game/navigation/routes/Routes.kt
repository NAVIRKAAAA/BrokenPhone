package com.brokentelephone.game.navigation.routes

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Welcome : Routes()

    @Serializable
    data object Dashboard : Routes()

    @Serializable
    data object CreatePost : Routes()

    @Serializable
    data class PostDetails(val postId: String) : Routes()

    @Serializable
    data class Draw(val postId: String) : Routes()

    @Serializable
    data class DescribeDrawing(val postId: String) : Routes()

    @Serializable
    data class ChainDetails(val postId: String) : Routes()

    @Serializable
    data object SignUp : Routes()

    @Serializable
    data object SignIn : Routes()

    @Serializable
    data object Profile : Routes()

    @Serializable
    data object EditUsername : Routes()

    @Serializable
    data object EditProfile : Routes()

    @Serializable
    data object EditAvatar : Routes()

    @Serializable
    data object ChooseAvatar : Routes()

    @Serializable
    data object ChooseUsername : Routes()

    @Serializable
    data object Settings : Routes()

    @Serializable
    data object AccountSettings : Routes()

    @Serializable
    data object BlockedUsers : Routes()

    @Serializable
    data object Notifications : Routes()

    @Serializable
    data object Language : Routes()

    @Serializable
    data object Theme : Routes()
}