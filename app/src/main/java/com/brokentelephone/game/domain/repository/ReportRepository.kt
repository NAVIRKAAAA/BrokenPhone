package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType

interface ReportRepository {

    suspend fun report(userId: String, postId: String, type: ReportPostType)

}
