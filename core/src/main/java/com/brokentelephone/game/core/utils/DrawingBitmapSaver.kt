package com.brokentelephone.game.core.utils

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class DrawingBitmapSaver(private val context: Context) {

    suspend fun save(bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        val file = File(context.cacheDir, "drawing_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        file.absolutePath
    }
}