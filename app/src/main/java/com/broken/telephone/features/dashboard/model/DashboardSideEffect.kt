package com.broken.telephone.features.dashboard.model

sealed interface DashboardSideEffect {
    data object ShowReportSuccessToast : DashboardSideEffect
    data object ShowNotInterestedToast : DashboardSideEffect
    data class ShowCopyLinkSuccessToast(val link: String) : DashboardSideEffect
}
