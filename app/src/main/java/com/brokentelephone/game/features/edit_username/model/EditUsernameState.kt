package com.brokentelephone.game.features.edit_username.model

import com.brokentelephone.game.features.choose_username.model.SuggestedUsernames
import com.brokentelephone.game.features.edit_username.EditUsernameViewModel

data class EditUsernameState(
    val username: String = "",
    val initialUsername: String = "",
    val suggestions: List<String> = SuggestedUsernames.random10(),
    val isLoading: Boolean = false,
    val globalError: String? = null,
) {
    val isSaveEnabled: Boolean
        get() = username.isNotBlank()
            && username.length <= EditUsernameViewModel.MAX_USERNAME_LENGTH
            && username != initialUsername
            && !isLoading
}
