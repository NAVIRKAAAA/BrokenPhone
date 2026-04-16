package com.brokentelephone.game.data.storage

import android.net.Uri
import android.util.Log
import com.brokentelephone.game.domain.storage.ImageStorage
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID

// 3781ms
class FirebaseImageStorage(
    private val storage: FirebaseStorage,
) : ImageStorage {

    override suspend fun uploadImage(localPath: String): String {
        val start = System.currentTimeMillis()

        val file = File(localPath)
        val ref = storage.reference
            .child(IMAGES_PATH)
            .child("${UUID.randomUUID()}.${file.extension}")

        ref.putFile(Uri.fromFile(file)).await()
        Log.d(TAG, "putFile: ${System.currentTimeMillis() - start}ms")

        val bucket = storage.app.options.storageBucket
        val encodedPath = Uri.encode("$IMAGES_PATH/${ref.name}")
        val url = "https://firebasestorage.googleapis.com/v0/b/$bucket/o/$encodedPath?alt=media"

        Log.d(TAG, "total: ${System.currentTimeMillis() - start}ms")
        return url
    }

    private companion object {
        const val IMAGES_PATH = "images"
        const val TAG = "FirebaseImageStorage"
    }
}
