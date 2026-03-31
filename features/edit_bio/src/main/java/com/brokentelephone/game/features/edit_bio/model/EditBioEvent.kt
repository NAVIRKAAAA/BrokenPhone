package com.brokentelephone.game.features.edit_bio.model

sealed class EditBioEvent {
    data object NavigateBack : EditBioEvent()
}
