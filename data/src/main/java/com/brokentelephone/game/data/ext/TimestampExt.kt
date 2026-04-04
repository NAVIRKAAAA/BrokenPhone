package com.brokentelephone.game.data.ext

import com.google.firebase.Timestamp

fun Long.toTimestamp(): Timestamp =
    Timestamp(this / 1000, ((this % 1000) * 1_000_000).toInt())

fun Timestamp.toMillis(): Long = toDate().time
