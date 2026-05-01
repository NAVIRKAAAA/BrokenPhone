package com.brokentelephone.game.features.user_friends.model

sealed interface UserFriendsSideEffect {
    data object NavigateBack : UserFriendsSideEffect
    data object ScrollToTop : UserFriendsSideEffect
}
