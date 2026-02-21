package com.broken.telephone.features.dashboard.model

import com.broken.telephone.features.profile.model.UserUi

data class DashboardState(
    val posts: List<PostUi> = emptyList(),
    val user: UserUi? = null,
)
