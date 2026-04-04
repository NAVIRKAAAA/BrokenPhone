package com.brokentelephone.game.di

import com.brokentelephone.game.data.google.GoogleSignInManagerImpl
import com.brokentelephone.game.data.handler.ApiHandlerImpl
import com.brokentelephone.game.data.link.LinkProviderImpl
import com.brokentelephone.game.data.repository.AuthRepositoryImpl
import com.brokentelephone.game.data.repository.FriendsRepositoryImpl
import com.brokentelephone.game.data.repository.GameSessionRepositoryImpl
import com.brokentelephone.game.data.repository.MockAppInfoRepositoryImpl
import com.brokentelephone.game.data.repository.NotificationsRepositoryImpl
import com.brokentelephone.game.data.repository.PostsRepositoryImpl
import com.brokentelephone.game.data.repository.ReportsRepositoryImpl
import com.brokentelephone.game.data.repository.UserSettingsRepositoryImpl
import com.brokentelephone.game.data.repository.UsersRepositoryImpl
import com.brokentelephone.game.data.session.UserSessionImpl
import com.brokentelephone.game.data.storage.FirebaseImageStorage
import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.google.GoogleSignInManager
import com.brokentelephone.game.domain.link.LinkProvider
import com.brokentelephone.game.domain.repository.AppInfoRepository
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.repository.ReportsRepository
import com.brokentelephone.game.domain.repository.UserSettingsRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.storage.ImageStorage
import com.brokentelephone.game.domain.use_case.UpdateFcmTokenUseCase
import com.brokentelephone.game.domain.user.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = firestoreSettings {
                setLocalCacheSettings(memoryCacheSettings {})
            }
        }
    }
    single { FirebaseStorage.getInstance() }
    single { FirebaseMessaging.getInstance() }

    single<ApiHandler> { ApiHandlerImpl() }
    single<UserSession> { UserSessionImpl(get(), get()) }
    single<LinkProvider> { LinkProviderImpl() }
    single<AppInfoRepository> { MockAppInfoRepositoryImpl() }
    single<ImageStorage> { FirebaseImageStorage(get()) }
    single<PostRepository> { PostsRepositoryImpl(get()) }
    single<GameSessionRepository> { GameSessionRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), androidContext()) }
    single<GoogleSignInManager> { GoogleSignInManagerImpl(androidContext()) }
    single<ReportsRepository> { ReportsRepositoryImpl(get()) }
    single<UserSettingsRepository> { UserSettingsRepositoryImpl(androidContext()) }
    single<UsersRepository> { UsersRepositoryImpl(get()) }
    single<FriendsRepository> { FriendsRepositoryImpl(get()) }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get()) }

    factoryOf(::UpdateFcmTokenUseCase)
}
