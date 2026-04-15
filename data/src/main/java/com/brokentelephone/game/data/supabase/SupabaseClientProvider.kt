package com.brokentelephone.game.data.supabase

import android.content.Context
import com.brokentelephone.game.data.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

fun provideSupabaseClient(context: Context) = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
) {
    install(Auth) {
        flowType = FlowType.PKCE
        sessionManager = SharedPreferencesSessionManager(context)
    }
    install(Postgrest)
    install(Realtime)
}
