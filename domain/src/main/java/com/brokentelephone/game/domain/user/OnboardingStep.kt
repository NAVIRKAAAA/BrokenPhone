package com.brokentelephone.game.domain.user

enum class OnboardingStep {
    CHOOSE_AVATAR,
    CHOOSE_USERNAME,
    COMPLETED;

    companion object {
        fun getByName(value: String): OnboardingStep =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: CHOOSE_USERNAME
    }
}
