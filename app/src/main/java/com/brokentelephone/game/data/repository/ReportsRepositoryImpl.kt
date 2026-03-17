package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.domain.repository.ReportsRepository
import com.brokentelephone.game.essentials.exceptions.auth.AlreadyReportedException
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

    override suspend fun report(userId: String, postId: String, type: ReportPostType) {
        try {
            val existingReport = collection.firestore
                .collection(COLLECTION_USERS)
                .document(userId)
                .collection(collectionName)
                .whereEqualTo(FIELD_POST_ID, postId)
                .limit(1)
                .get()
                .await()

            if (!existingReport.isEmpty) throw AlreadyReportedException()

            val reportDoc = collection.document()
            val userReportDoc = reportDoc.firestore
                .collection(COLLECTION_USERS)
                .document(userId)
                .collection(collectionName)
                .document(reportDoc.id)
            val postReportDoc = reportDoc.firestore
                .collection(COLLECTION_POSTS)
                .document(postId)
                .collection(collectionName)
                .document(reportDoc.id)

            val data = mapOf(
                FIELD_USER_ID to userId,
                FIELD_POST_ID to postId,
                FIELD_REPORT_TYPE to type.name,
                FIELD_CREATED_AT to System.currentTimeMillis().toTimestamp(),
            )

            reportDoc.firestore.runBatch { batch ->
                batch.set(reportDoc, data)
                batch.set(userReportDoc, data)
                batch.set(postReportDoc, data)
            }.await()
        } catch (e: AppException) {
            throw e
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "report(): $e")
            throw UnknownAuthException()
        }
    }

    private companion object {
        const val COLLECTION_USERS = "users"
        const val COLLECTION_POSTS = "posts"
        const val FIELD_USER_ID = "userId"
        const val FIELD_POST_ID = "postId"
        const val FIELD_REPORT_TYPE = "reportType"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
