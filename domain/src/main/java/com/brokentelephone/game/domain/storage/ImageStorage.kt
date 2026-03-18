package com.brokentelephone.game.domain.storage

interface ImageStorage {

    suspend fun uploadImage(localPath: String): String

}
