package com.broken.telephone.features.post_details.model

enum class PostDetailsButtonType(
    val buttonText: String,
    val isEnabled: Boolean,
    val description: (timeLimit: Int) -> String,
) {

    DRAW(
        buttonText = "Continue",
        isEnabled = true,
        description = { timeLimit -> "Draw this in $timeLimit seconds" },
    ),

    DESCRIBE(
        buttonText = "Continue",
        isEnabled = true,
        description = { timeLimit -> "Describe in $timeLimit seconds" },
    ),

    UNAVAILABLE(
        buttonText = "Continue",
        isEnabled = false,
        description = { _ -> "Someone is working on this right now" },
    ),

    COMPLETED(
        buttonText = "View",
        isEnabled = true,
        description = { _ -> "The game has been completed" },
    ),

    OWN_POST(
        buttonText = "Continue",
        isEnabled = false,
        description = { _ -> "You can't continue your own post" },
    ),

}
