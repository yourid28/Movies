@file:Suppress("RemoveExplicitTypeArguments")

package com.yourid.movieshowcase.core.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.yourid.movieshowcase.core.model.network.ApiDataProvider
import com.yourid.movieshowcase.core.model.network.ApiDataProviderImpl
import com.yourid.movieshowcase.core.model.network.ApiInterface
import com.yourid.movieshowcase.core.model.network.ApiProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_IN_SECOND = 10
private const val API_KEY = "api_key"

val networkModule = module {

    single<ApiDataProvider> { ApiDataProviderImpl() }

    factory<OkHttpClient> { buildOkHttpClient(get<ApiDataProvider>()) }
    factory<Retrofit> { buildRetrofit(get<ApiDataProvider>(), get<OkHttpClient>()) }
    factory<ApiInterface> { ApiProvider(get<Retrofit>()).provide() }
}

private val loggingInterceptor = HttpLoggingInterceptor()

private fun buildOkHttpClient(apiDataProvider: ApiDataProvider): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .addInterceptor(loggingInterceptor)
        .addInterceptor(getApiKeyInterceptor(apiDataProvider.provideApiKey()))
        .readTimeout(TIMEOUT_IN_SECOND.toLong(), TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT_IN_SECOND.toLong(), TimeUnit.SECONDS)
        .build()
}

private fun getApiKeyInterceptor(apiKey: String) = Interceptor { chain ->
    val url = chain.request()
        .url()
        .newBuilder()
        .addQueryParameter(API_KEY, apiKey)
        .build()

    val newRequest = chain.request()
        .newBuilder()
        .url(url)
        .build()

    chain.proceed(newRequest)
}

private fun buildRetrofit(apiDataProvider: ApiDataProvider, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(apiDataProvider.provideBaseUrl())
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}