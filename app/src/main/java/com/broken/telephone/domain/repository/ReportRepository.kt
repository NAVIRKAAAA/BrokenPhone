package com.broken.telephone.domain.repository

import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType

interface ReportRepository {

    suspend fun report(userId: String, postId: String, type: ReportPostType)

}
