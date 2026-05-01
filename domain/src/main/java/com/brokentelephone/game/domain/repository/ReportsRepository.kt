package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.model.report.ReportUserType

interface ReportsRepository {

    suspend fun reportPost(userId: String, postId: String, type: ReportPostType)

    suspend fun reportUser(userId: String, targetUserId: String, type: ReportUserType)

}
