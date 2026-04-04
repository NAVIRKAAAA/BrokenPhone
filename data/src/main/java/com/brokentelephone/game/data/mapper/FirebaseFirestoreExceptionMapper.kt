package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreAbortedException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreAlreadyExistsException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreCancelledException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreDeadlineExceededException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreFailedPreconditionException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreInternalException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreInvalidArgumentException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreNotFoundException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestorePermissionDeniedException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreResourceExhaustedException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreUnauthenticatedException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreUnavailableException
import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.google.firebase.firestore.FirebaseFirestoreException


fun FirebaseFirestoreException.toAppException(): AppException =
    when (code) {
        FirebaseFirestoreException.Code.CANCELLED -> FirestoreCancelledException()
        FirebaseFirestoreException.Code.INVALID_ARGUMENT -> FirestoreInvalidArgumentException()
        FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> FirestoreDeadlineExceededException()
        FirebaseFirestoreException.Code.NOT_FOUND -> FirestoreNotFoundException()
        FirebaseFirestoreException.Code.ALREADY_EXISTS -> FirestoreAlreadyExistsException()
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> FirestorePermissionDeniedException()
        FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> FirestoreResourceExhaustedException()
        FirebaseFirestoreException.Code.FAILED_PRECONDITION -> FirestoreFailedPreconditionException()
        FirebaseFirestoreException.Code.ABORTED -> FirestoreAbortedException()
        FirebaseFirestoreException.Code.UNAUTHENTICATED -> FirestoreUnauthenticatedException()
        FirebaseFirestoreException.Code.UNAVAILABLE -> FirestoreUnavailableException()
        else -> FirestoreInternalException()
    }