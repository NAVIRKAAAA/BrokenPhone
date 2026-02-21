package com.broken.telephone.core.bottom_sheet.post_bottom_sheet.model

import com.broken.telephone.R

enum class PostBottomSheetAction(val label: String, val iconResId: Int) {
    NOT_INTERESTED("Not Interested", R.drawable.ic_not_interested),
    BLOCK("Block", R.drawable.ic_block),
    REPORT("Report", R.drawable.ic_report),
}
