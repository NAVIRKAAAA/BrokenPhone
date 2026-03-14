package com.brokentelephone.game.data.storage

import android.net.Uri
import com.brokentelephone.game.domain.storage.ImageStorage
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID

class FirebaseImageStorage(
    private val storage: FirebaseStorage,
) : ImageStorage {

    override suspend fun uploadImage(localPath: String): String {
        val file = File(localPath)
        val ref = storage.reference
            .child(IMAGES_PATH)
            .child("${UUID.randomUUID()}.${file.extension}")

        ref.putFile(Uri.fromFile(file)).await()
        return ref.downloadUrl.await().toString()
    }

    private companion object {
        const val IMAGES_PATH = "images"
    }
}
