package com.brokentelephone.game.domain.model.report

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R

enum class ReportUserType(@param:StringRes override val labelResId: Int) : ReportType {
    SPAM(R.string.report_post_bottom_sheet_spam),
    OFFENSIVE_CONTENT(R.string.report_post_bottom_sheet_offensive_content),
    OTHER(R.string.report_post_bottom_sheet_other),
}
