package com.brokentelephone.game.features.chain_details.model

sealed interface ChainDetailsSideEffect {
    data object NavigateBack : ChainDetailsSideEffect
}
