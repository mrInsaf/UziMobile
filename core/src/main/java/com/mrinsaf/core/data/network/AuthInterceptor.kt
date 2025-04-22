package com.mrinsaf.core.data.network

import android.content.Context
import com.mrinsaf.core.data.repository.local.TokenStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    @ApplicationContext val context: Context,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenStorage.getAccessToken(context)
        val newRequest = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}