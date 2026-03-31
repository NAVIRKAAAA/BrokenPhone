package com.brokentelephone.game.features.edit_bio.model

import com.brokentelephone.game.features.edit_bio.EditBioViewModel

data class EditBioState(
    val bio: String = "",
    val initialBio: String = "",
    val isLoading: Boolean = false,
    val globalError: String? = null,
) {
    val isSaveEnabled: Boolean
        get() = bio.length <= EditBioViewModel.MAX_BIO_LENGTH
            && bio != initialBio
            && !isLoading
}
