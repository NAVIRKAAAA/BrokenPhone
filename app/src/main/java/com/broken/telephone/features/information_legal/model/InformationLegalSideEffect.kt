package com.broken.telephone.features.information_legal.model

sealed interface InformationLegalSideEffect {
    data class OpenLink(val url: String) : InformationLegalSideEffect
}
