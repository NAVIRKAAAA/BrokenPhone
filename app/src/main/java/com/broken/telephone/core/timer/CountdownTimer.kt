package com.broken.telephone.core.timer

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountdownTimer {

    fun start(durationSeconds: Int): Flow<Int> = flow {
        for (remaining in durationSeconds downTo 0) {
            emit(remaining)
            if (remaining > 0) delay(1_000)
        }
    }

}
