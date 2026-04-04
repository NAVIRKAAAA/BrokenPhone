package com.brokentelephone.game.features.edit_bio.di

import com.brokentelephone.game.edit_bio_api.EditBioNavigationApi
import com.brokentelephone.game.features.edit_bio.EditBioViewModel
import com.brokentelephone.game.features.edit_bio.api.EditBioNavigationApiImpl
import com.brokentelephone.game.features.edit_bio.use_case.UpdateBioUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val editBioModule = module {
    viewModelOf(::EditBioViewModel)
    factoryOf(::UpdateBioUseCase)

    single<EditBioNavigationApi> { EditBioNavigationApiImpl() }
}
