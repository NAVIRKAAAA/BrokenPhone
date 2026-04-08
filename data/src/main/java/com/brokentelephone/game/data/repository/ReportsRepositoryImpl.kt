package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.model.report.ReportTargetType
import com.brokentelephone.game.domain.model.report.ReportUserType
import com.brokentelephone.game.domain.repository.ReportsRepository
import com.brokentelephone.game.essentials.exceptions.auth.AlreadyReportedPostException
import com.brokentelephone.game.essentials.exceptions.auth.AlreadyReportedUserException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class ReportsRepositoryImpl(
    firestore: FirebaseFirestore,
) : FirestoreRepository(firestore), ReportsRepository {

    override val collectionName = "reports"

    override suspend fun reportPost(userId: String, postId: String, type: ReportPostType) {
        try {
            val existingReport = collection
                .whereEqualTo(FIELD_REPORTER_ID, userId)
                .whereEqualTo(FIELD_TARGET_ID, postId)
                .limit(1)
                .getFromServer()

            if (!existingReport.isEmpty) throw AlreadyReportedPostException()

            collection.document().set(
                mapOf(
                    FIELD_REPORTER_ID to userId,
                    FIELD_TARGET_ID to postId,
                    FIELD_TARGET_TYPE to ReportTargetType.POST.name,
                    FIELD_REPORT_SUBTYPE to type.name,
                    FIELD_CREATED_AT to System.currentTimeMillis().toTimestamp(),
                )
            ).await()
        } catch (e: AppException) {
            throw e
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "reportPost(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun reportUser(userId: String, targetUserId: String, type: ReportUserType) {
        try {
            val existingReport = collection
                .whereEqualTo(FIELD_REPORTER_ID, userId)
                .whereEqualTo(FIELD_TARGET_ID, targetUserId)
                .limit(1)
                .getFromServer()

            if (!existingReport.isEmpty) throw AlreadyReportedUserException()

            collection.document().set(
                mapOf(
                    FIELD_REPORTER_ID to userId,
                    FIELD_TARGET_ID to targetUserId,
                    FIELD_TARGET_TYPE to ReportTargetType.USER.name,
                    FIELD_REPORT_SUBTYPE to type.name,
                    FIELD_CREATED_AT to System.currentTimeMillis().toTimestamp(),
                )
            ).await()
        } catch (e: AppException) {
            throw e
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "reportUser(): $e")
            throw UnknownAuthException()
        }

    }

    private companion object {
        const val FIELD_REPORTER_ID = "reporterId"
        const val FIELD_TARGET_ID = "targetId"
        const val FIELD_TARGET_TYPE = "targetType"
        const val FIELD_REPORT_SUBTYPE = "reportSubtype"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
