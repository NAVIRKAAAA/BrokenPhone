package com.brokentelephone.game.features.friends.model

sealed interface FriendsSideEffect {
    data object NavigateBack : FriendsSideEffect
    data object ScrollToTop : FriendsSideEffect
}
