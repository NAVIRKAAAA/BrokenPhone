package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.domain.post.PostContent

private object PostContentFields {
    const val TYPE = "type"
    const val TEXT = "text"
    const val IMAGE_URL = "imageUrl"
    const val TYPE_TEXT = "text"
    const val TYPE_DRAWING = "drawing"
}

fun PostContent.toMap(): Map<String, Any?> = when (this) {
    is PostContent.Text -> mapOf(PostContentFields.TYPE to PostContentFields.TYPE_TEXT, PostContentFields.TEXT to text)
    is PostContent.Drawing -> mapOf(PostContentFields.TYPE to PostContentFields.TYPE_DRAWING, PostContentFields.IMAGE_URL to imageUrl)
}

@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.toPostContent(): PostContent? {
    return when (this[PostContentFields.TYPE]) {
        PostContentFields.TYPE_TEXT -> PostContent.Text(text = this[PostContentFields.TEXT] as? String ?: return null)
        PostContentFields.TYPE_DRAWING -> PostContent.Drawing(imageUrl = this[PostContentFields.IMAGE_URL] as? String)
        else -> null
    }
}
