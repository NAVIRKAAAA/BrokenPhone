package com.brokentelephone.game.essentials.exceptions.firestore

import com.brokentelephone.game.essentials.R
import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.brokentelephone.game.essentials.exceptions.main.StringProvider

// CANCELLED (1)
class FirestoreCancelledException : AppException("Firestore operation was cancelled") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_cancelled)
}

// INVALID_ARGUMENT (3)
class FirestoreInvalidArgumentException : AppException("Firestore invalid argument") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_invalid_argument)
}

// DEADLINE_EXCEEDED (4)
class FirestoreDeadlineExceededException : AppException("Firestore deadline exceeded") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_deadline_exceeded)
}

// NOT_FOUND (5)
class FirestoreNotFoundException : AppException("Firestore document not found") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_not_found)
}

// ALREADY_EXISTS (6)
class FirestoreAlreadyExistsException : AppException("Firestore document already exists") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_already_exists)
}

// PERMISSION_DENIED (7)
class FirestorePermissionDeniedException : AppException("Firestore permission denied") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_permission_denied)
}

// RESOURCE_EXHAUSTED (8)
class FirestoreResourceExhaustedException : AppException("Firestore resource exhausted") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_resource_exhausted)
}

// FAILED_PRECONDITION (9)
class FirestoreFailedPreconditionException : AppException("Firestore failed precondition") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_failed_precondition)
}

// ABORTED (10)
class FirestoreAbortedException : AppException("Firestore operation aborted") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_aborted)
}

// UNAUTHENTICATED (16)
class FirestoreUnauthenticatedException : AppException("Firestore unauthenticated") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_unauthenticated)
}

// UNAVAILABLE (14)
class FirestoreUnavailableException : AppException("Firestore service unavailable") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_unavailable)
}

// INTERNAL / DATA_LOSS / UNKNOWN / UNIMPLEMENTED / OUT_OF_RANGE / etc.
class FirestoreInternalException : AppException("Firestore internal error") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_firestore_internal)
}