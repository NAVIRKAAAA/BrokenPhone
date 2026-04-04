package com.brokentelephone.game.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

abstract class FirestoreRepository(
    private val firestore: FirebaseFirestore,
) {
    abstract val collectionName: String

    protected val collection: CollectionReference
        get() = firestore.collection(collectionName)

    protected suspend fun <T> whereInChunked(
        field: String,
        ids: List<String>,
        mapper: (Map<String, Any?>) -> T?,
    ): List<T> {
        if (ids.isEmpty()) return emptyList()
        return coroutineScope {
            ids.chunked(10)
                .map { chunk ->
                    async {
                        collection.whereIn(field, chunk).get().await()
                            .documents
                            .mapNotNull { it.data?.let(mapper) }
                    }
                }
                .awaitAll()
                .flatten()
        }
    }
}
