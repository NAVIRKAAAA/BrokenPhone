package com.brokentelephone.game.domain.model.report

import androidx.annotation.StringRes

interface ReportType {
    @get:StringRes
    val labelResId: Int
}
