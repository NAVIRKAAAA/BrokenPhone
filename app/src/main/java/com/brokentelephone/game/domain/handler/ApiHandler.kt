package com.brokentelephone.game.domain.handler

import kotlinx.coroutines.CoroutineDispatcher

private const val DEFAULT_MAX_RETRIES = 1

interface ApiHandler {
    suspend fun <T> handle(
        dispatcher: CoroutineDispatcher,
        maxRetries: Int = DEFAULT_MAX_RETRIES,
        block: suspend () -> T,
    ): AppResult<T>
}