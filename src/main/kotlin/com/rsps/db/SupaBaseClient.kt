package com.rsps.db

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.minimalSettings
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.engine.okhttp.*

class SupaBaseClient(
    url: String,
    key: String
) {

    val client = createSupabaseClient(
        supabaseUrl = url,
        supabaseKey = key
    ) {
        httpEngine = OkHttp.create {
            config {
                followRedirects(true)
                retryOnConnectionFailure(true)
            }
        }
        install(Auth) {
            minimalSettings()
        }
        install(Postgrest)
    }
}