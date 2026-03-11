package com.brokentelephone.game.domain.handler

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val exception: Exception) : AppResult<Nothing>()
}

suspend fun <T> AppResult<T>.onSuccess(action: suspend (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

suspend fun <T> AppResult<T>.onError(action: suspend (Exception) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(exception)
    return this
}