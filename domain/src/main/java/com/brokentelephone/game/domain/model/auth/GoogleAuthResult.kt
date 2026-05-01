package com.brokentelephone.game.domain.model.auth

data class GoogleAuthResult(
    val uid: String,
    val email: String,
    val isNewUser: Boolean,
)
