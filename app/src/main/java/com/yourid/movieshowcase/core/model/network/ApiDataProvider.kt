package com.yourid.movieshowcase.core.model.network

import com.yourid.movieshowcase.R
import com.yourid.movieshowcase.core.application.App

private const val BASE_URL = "https://api.themoviedb.org/3/"

interface ApiDataProvider {

    fun provideBaseUrl(): String

    fun provideApiKey(): String
}

class ApiDataProviderImpl : ApiDataProvider {

    override fun provideBaseUrl(): String = BASE_URL

    override fun provideApiKey(): String =
        App.applicationContext().getString(R.string.api_key)
}