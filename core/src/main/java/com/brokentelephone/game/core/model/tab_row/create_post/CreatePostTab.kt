package com.brokentelephone.game.core.model.tab_row.create_post

import androidx.annotation.StringRes
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.tab_row.BTTabRow

enum class CreatePostTab(@param:StringRes override val labelResId: Int) : BTTabRow {
    TEXT(R.string.create_post_tab_text),
    DRAW(R.string.create_post_tab_draw),
}
