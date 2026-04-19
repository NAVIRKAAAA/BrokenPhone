package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.dto.ReportDto
import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.model.report.ReportTargetType
import com.brokentelephone.game.domain.model.report.ReportUserType
import com.brokentelephone.game.domain.repository.ReportsRepository
import com.brokentelephone.game.essentials.exceptions.auth.AlreadyReportedPostException
import com.brokentelephone.game.essentials.exceptions.auth.AlreadyReportedUserException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.main.AppException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import java.io.IOException

class ReportsRepositoryImpl(
    private val supabase: SupabaseClient,
) : ReportsRepository {

    override suspend fun reportPost(userId: String, postId: String, type: ReportPostType) {
        try {
            val existing = supabase.from(TABLE_REPORTS)
                .select {
                    filter {
                        eq(FIELD_REPORTER_ID, userId)
                        eq(FIELD_TARGET_ID, postId)
                    }
                    limit(1)
                }
                .decodeSingleOrNull<ReportDto>()

            if (existing != null) throw AlreadyReportedPostException()

            supabase.from(TABLE_REPORTS).insert(
                ReportDto(
                    reporterId = userId,
                    targetId = postId,
                    targetType = ReportTargetType.POST.name,
                    reportSubtype = type.name,
                    createdAt = System.currentTimeMillis(),
                )
            )
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "reportPost(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "reportPost(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun reportUser(userId: String, targetUserId: String, type: ReportUserType) {
        try {
            val existing = supabase.from(TABLE_REPORTS)
                .select {
                    filter {
                        eq(FIELD_REPORTER_ID, userId)
                        eq(FIELD_TARGET_ID, targetUserId)
                    }
                    limit(1)
                }
                .decodeSingleOrNull<ReportDto>()

            if (existing != null) throw AlreadyReportedUserException()

            supabase.from(TABLE_REPORTS).insert(
                ReportDto(
                    reporterId = userId,
                    targetId = targetUserId,
                    targetType = ReportTargetType.USER.name,
                    reportSubtype = type.name,
                    createdAt = System.currentTimeMillis(),
                )
            )
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "reportUser(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "reportUser(): $e")
            throw UnknownAuthException()
        }
    }

    private companion object {
        const val TABLE_REPORTS = "reports"
        const val FIELD_REPORTER_ID = "reporter_id"
        const val FIELD_TARGET_ID = "target_id"
    }
}
