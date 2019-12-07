package com.yourid.movieshowcase.core.common

interface Mapper<E, T> {

    fun mapFrom(source: E): T
}