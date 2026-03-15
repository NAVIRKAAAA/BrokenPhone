package com.brokentelephone.game.data.handler

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.essentials.exceptions.auth.OperationCancelledException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val DEFAULT_TIMEOUT_MS = 10_000L
private const val DEFAULT_INITIAL_DELAY_MS = 500L
private const val DEFAULT_BACKOFF_FACTOR = 2.0

class ApiHandlerImpl(
    private val timeoutMs: Long = DEFAULT_TIMEOUT_MS,
    private val initialDelayMs: Long = DEFAULT_INITIAL_DELAY_MS,
    private val backoffFactor: Double = DEFAULT_BACKOFF_FACTOR,
) : ApiHandler {

    override suspend fun <T> handle(
        dispatcher: CoroutineDispatcher,
        maxRetries: Int,
        block: suspend () -> T,
    ): AppResult<T> = withContext(dispatcher) {
        var currentDelay = initialDelayMs

        for (attempt in 0..maxRetries) {
            try {
                val result = withTimeout(timeoutMs) { block() }
                return@withContext AppResult.Success(result)
            } catch (_: CancellationException) {
                throw OperationCancelledException()
            } catch (_: TimeoutCancellationException) {
                throw OperationCancelledException()
            } catch (e: Exception) {
                if (attempt == maxRetries) return@withContext AppResult.Error(e)
                delay(currentDelay)
                currentDelay = (currentDelay * backoffFactor).toLong()
            }
        }

        error("Unreachable")
    }
}
