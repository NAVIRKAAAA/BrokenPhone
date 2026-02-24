package com.broken.telephone.core.bottom_sheet.post_bottom_sheet.model

import androidx.annotation.StringRes
import com.broken.telephone.R

enum class PostBottomSheetAction(
    @param:StringRes val labelResId: Int, val iconResId: Int
) {
    COPY_LINK(R.string.post_bottom_sheet_copy_link, R.drawable.ic_link),
    NOT_INTERESTED(R.string.post_bottom_sheet_not_interested, R.drawable.ic_not_interested),
    BLOCK(R.string.common_block, R.drawable.ic_block),
    REPORT(R.string.post_bottom_sheet_report, R.drawable.ic_report),
    DELETE(R.string.common_delete, R.drawable.ic_trash),
}
