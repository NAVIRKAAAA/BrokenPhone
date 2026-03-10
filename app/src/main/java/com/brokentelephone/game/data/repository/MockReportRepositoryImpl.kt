package com.brokentelephone.game.data.repository

import com.brokentelephone.game.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.brokentelephone.game.domain.repository.ReportRepository
import kotlinx.coroutines.delay

class MockReportRepositoryImpl : ReportRepository {

    override suspend fun report(userId: String, postId: String, type: ReportPostType) {
        delay(1500)
    }

}
