package com.broken.telephone.features.describe_drawing.model

sealed interface DescribeDrawingSideEffect {
    data object PostCreated : DescribeDrawingSideEffect
}
