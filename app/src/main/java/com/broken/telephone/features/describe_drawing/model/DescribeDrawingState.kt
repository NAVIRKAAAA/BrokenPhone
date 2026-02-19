package com.broken.telephone.features.describe_drawing.model

import com.broken.telephone.features.dashboard.model.PostUi

data class DescribeDrawingState(
    val postUi: PostUi? = null,
    val text: String = "",
) {
    val isTextOverLimit: Boolean get() = text.length > MAX_TEXT_LENGTH

    companion object {
        const val MAX_TEXT_LENGTH = 140
    }
}
