package com.brokentelephone.game.domain.handler

import kotlinx.coroutines.CoroutineDispatcher

interface ApiHandler {
    suspend fun <T> handle(
        dispatcher: CoroutineDispatcher,
        block: suspend () -> T,
    ): AppResult<T>
}