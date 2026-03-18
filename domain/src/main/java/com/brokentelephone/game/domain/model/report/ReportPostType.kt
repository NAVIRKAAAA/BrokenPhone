package com.brokentelephone.game.domain.model.report

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R

enum class ReportPostType(@param:StringRes val labelResId: Int) {
    SPAM(R.string.report_post_bottom_sheet_spam),
    OFFENSIVE_CONTENT(R.string.report_post_bottom_sheet_offensive_content),
    OTHER(R.string.report_post_bottom_sheet_other),
}