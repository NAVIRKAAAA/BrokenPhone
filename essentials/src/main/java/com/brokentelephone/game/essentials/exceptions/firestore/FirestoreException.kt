package com.brokentelephone.game.essentials.exceptions.firestore

import com.brokentelephone.game.essentials.R
import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.brokentelephone.game.essentials.exceptions.main.string_provider.StringProvider

class FirestoreCancelledException : AppException("Firestore operation was cancelled") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_cancelled)
}

class FirestoreInvalidArgumentException : AppException("Firestore invalid argument") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_invalid_argument)
}

class FirestoreDeadlineExceededException : AppException("Firestore deadline exceeded") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_deadline_exceeded)
}

class FirestoreNotFoundException : AppException("Firestore document not found") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_not_found)
}

class FirestoreAlreadyExistsException : AppException("Firestore document already exists") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_already_exists)
}

class FirestorePermissionDeniedException : AppException("Firestore permission denied") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_permission_denied)
}

class FirestoreResourceExhaustedException : AppException("Firestore resource exhausted") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_resource_exhausted)
}

class FirestoreFailedPreconditionException : AppException("Firestore failed precondition") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_failed_precondition)
}

class FirestoreAbortedException : AppException("Firestore operation aborted") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_aborted)
}

class FirestoreUnauthenticatedException : AppException("Firestore unauthenticated") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_unauthenticated)
}

class FirestoreUnavailableException : AppException("Firestore service unavailable") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_unavailable)
}

class FirestoreInternalException : AppException("Firestore internal error") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_internal)
}