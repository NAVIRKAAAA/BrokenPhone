package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.report.ReportPostType

interface ReportsRepository {

    suspend fun report(userId: String, postId: String, type: ReportPostType)

}
