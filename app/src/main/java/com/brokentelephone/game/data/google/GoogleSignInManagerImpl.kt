package com.brokentelephone.game.data.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.brokentelephone.game.BuildConfig
import com.brokentelephone.game.domain.google.GoogleSignInManager
import com.brokentelephone.game.essentials.exceptions.auth.GoogleSignInCancelledException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleSignInManagerImpl(
    private val context: Context,
) : GoogleSignInManager {

    override suspend fun getIdToken(): String {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(context, request)
            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
            return credential.idToken
        } catch (_: GetCredentialCancellationException) {
            throw GoogleSignInCancelledException()
        }
    }
}
