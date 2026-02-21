package com.broken.telephone.data.repository

import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.broken.telephone.domain.repository.ReportRepository
import kotlinx.coroutines.delay

class MockReportRepositoryImpl : ReportRepository {

    override suspend fun report(userId: String, postId: String, type: ReportPostType) {
        delay(1500)
    }

}
