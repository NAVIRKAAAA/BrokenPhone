package com.brokentelephone.game.features.add_friend.model

sealed interface AddFriendSideEffect {
    data object ScrollToTop : AddFriendSideEffect
}
