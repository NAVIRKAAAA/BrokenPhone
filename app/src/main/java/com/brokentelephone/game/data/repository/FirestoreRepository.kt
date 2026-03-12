package com.brokentelephone.game.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

abstract class FirestoreRepository(
    private val firestore: FirebaseFirestore,
) {
    abstract val collectionName: String

    protected val collection: CollectionReference
        get() = firestore.collection(collectionName)
}
