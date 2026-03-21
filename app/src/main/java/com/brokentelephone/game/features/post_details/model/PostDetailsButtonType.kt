package com.brokentelephone.game.features.post_details.model

import com.brokentelephone.game.core.R

enum class PostDetailsButtonType(
    val buttonTextResId: Int,
    val isEnabled: Boolean,
    val descriptionResId: Int,
) {

    DRAW(
        buttonTextResId = R.string.post_details_button_continue,
        isEnabled = true,
        descriptionResId = R.string.post_details_description_draw,
    ),

    DESCRIBE(
        buttonTextResId = R.string.post_details_button_continue,
        isEnabled = true,
        descriptionResId = R.string.post_details_description_describe,
    ),

    UNAVAILABLE(
        buttonTextResId = R.string.post_details_button_continue,
        isEnabled = false,
        descriptionResId = R.string.post_details_description_unavailable,
    ),

    COMPLETED(
        buttonTextResId = R.string.post_details_button_view,
        isEnabled = true,
        descriptionResId = R.string.post_details_description_completed,
    ),

    OWN_POST(
        buttonTextResId = R.string.post_details_button_continue,
//        isEnabled = false,
        isEnabled = true,
        descriptionResId = R.string.post_details_description_own_post,
    ),

    COOLDOWN(
        buttonTextResId = R.string.post_details_button_cooldown,
        isEnabled = false,
        descriptionResId = R.string.post_details_description_cooldown,
    ),

    MY_SESSION(
        buttonTextResId = R.string.post_details_button_my_session,
        isEnabled = true,
        descriptionResId = R.string.post_details_description_my_session,
    ),

}
