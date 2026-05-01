package com.brokentelephone.game.features.bottom_nav_bar.di

import com.brokentelephone.game.features.bottom_nav_bar.AppNavBottomBarViewModel
import org.koin.dsl.module

val bottomNavBarModule = module {
    single {
        AppNavBottomBarViewModel(
            get(),
        )
    }
}
