package com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model

import androidx.annotation.StringRes
import com.broken.telephone.R

enum class ReportPostType(@StringRes val labelResId: Int) {
    SPAM(R.string.report_post_bottom_sheet_spam),
    INAPPROPRIATE_CONTENT(R.string.report_post_bottom_sheet_inappropriate_content),
    HARASSMENT(R.string.report_post_bottom_sheet_harassment),
    OTHER(R.string.report_post_bottom_sheet_other),
}
