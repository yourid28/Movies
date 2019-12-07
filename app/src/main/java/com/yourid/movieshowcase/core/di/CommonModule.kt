@file:Suppress("RemoveExplicitTypeArguments")

package com.yourid.movieshowcase.core.di

import com.squareup.picasso.Picasso
import com.yourid.movieshowcase.core.common.ImageLoader
import com.yourid.movieshowcase.core.common.ImageLoaderImpl
import org.koin.dsl.module

val commonModule = module {

    factory<Picasso> { Picasso.get() }
    factory<ImageLoader> { ImageLoaderImpl(get<Picasso>()) }
}