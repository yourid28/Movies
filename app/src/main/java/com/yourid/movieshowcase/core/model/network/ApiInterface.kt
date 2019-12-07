package com.yourid.movieshowcase.core.model.network

import com.yourid.movieshowcase.core.model.network.models.ConfigResponse
import com.yourid.movieshowcase.core.model.network.models.MovieDetailsResponse
import com.yourid.movieshowcase.core.model.network.models.MovieImageResponse
import com.yourid.movieshowcase.core.model.network.models.MoviesResultResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("configuration")
    fun getConfigsAsync(): Deferred<Response<ConfigResponse>>

    @GET("movie/now_playing/")
    fun getNowPlayingMoviesAsync(): Deferred<Response<MoviesResultResponse>>

    @GET("movie/popular/")
    fun getPopularMoviesAsync(): Deferred<Response<MoviesResultResponse>>

    @GET("movie/top_rated/")
    fun getTopRatedMoviesAsync(): Deferred<Response<MoviesResultResponse>>

    @GET("movie/upcoming/")
    fun getUpcomingMoviesAsync(): Deferred<Response<MoviesResultResponse>>

    @GET("movie/{id}?")
    fun getMovieByIdAsync(@Path("id") movieId: Int): Deferred<Response<MovieDetailsResponse>>

    @GET("movie/{id}/images?")
    fun getMovieImagesAsync(@Path("id") movieId: Int): Deferred<Response<MovieImageResponse>>

    @GET("search/movie")
    fun searchMovieAsync(@Query("query") query: String): Deferred<Response<MoviesResultResponse>>
}