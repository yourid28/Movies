package com.yourid.movieshowcase.core.model.network

interface ArgumentsWrapper

data class MovieIdArgs(val movieId: Int) : ArgumentsWrapper

data class SearchQueryArgs(val query: String) : ArgumentsWrapper