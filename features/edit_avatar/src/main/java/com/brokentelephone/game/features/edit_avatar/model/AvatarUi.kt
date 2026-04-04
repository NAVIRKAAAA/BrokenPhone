package com.brokentelephone.game.features.edit_avatar.model

data class AvatarUi(
    val id: Int,
    val url: String,
)

object Avatars {
    val all: List<AvatarUi> = buildList {
        (1..27).forEach { index ->
            add(AvatarUi(id = index, url = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_$index.png"))
        }
        (1..9).forEach { index ->
            add(AvatarUi(id = 100 + index, url = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/teams_$index.png"))
        }
        (1..10).forEach { index ->
            add(AvatarUi(id = 200 + index, url = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/toon_$index.png"))
        }
        (1..21).forEach { index ->
            add(AvatarUi(id = 300 + index, url = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_$index.png"))
        }
    }
}
