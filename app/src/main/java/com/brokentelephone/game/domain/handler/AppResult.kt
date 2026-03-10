package com.brokentelephone.game.domain.handler

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val exception: Exception) : AppResult<Nothing>()
}

fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

fun <T> AppResult<T>.onError(action: (Exception) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(exception)
    return this
}