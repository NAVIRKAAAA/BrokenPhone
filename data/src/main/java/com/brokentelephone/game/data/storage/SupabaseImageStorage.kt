package com.brokentelephone.game.data.storage

import android.util.Log
import com.brokentelephone.game.domain.storage.ImageStorage
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import java.io.File
import java.io.IOException
import java.util.UUID

// 655ms
class SupabaseImageStorage(
    private val supabase: SupabaseClient,
) : ImageStorage {

    override suspend fun uploadImage(localPath: String): String {
        try {
            val start = System.currentTimeMillis()
            val file = File(localPath)
            val fileName = "${UUID.randomUUID()}.${file.extension}"
            supabase.storage.from(BUCKET).upload(fileName, file.readBytes())
            Log.d(TAG, "upload: ${System.currentTimeMillis() - start}ms")
            val url = supabase.storage.from(BUCKET).publicUrl(fileName)
            Log.d(TAG, "total: ${System.currentTimeMillis() - start}ms")
            return url
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "uploadImage: $e")
            throw UnknownAuthException()
        }
    }

    private companion object {
        const val BUCKET = "images"
        const val TAG = "SupabaseImageStorage"
    }
}
