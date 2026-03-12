package com.brokentelephone.game.features.choose_username.model

import com.brokentelephone.game.features.choose_username.ChooseUsernameViewModel

data class ChooseUsernameState(
    val username: String = "",
    val suggestions: List<String> = SuggestedUsernames.random10(),
    val isLoading: Boolean = false,
    val globalError: String? = null,
) {
    val isContinueEnabled: Boolean
        get() = username.isNotBlank() && username.length <= ChooseUsernameViewModel.MAX_USERNAME_LENGTH && !isLoading
}
