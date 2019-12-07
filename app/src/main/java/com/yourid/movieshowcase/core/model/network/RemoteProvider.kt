package com.yourid.movieshowcase.core.model.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yourid.movieshowcase.core.common.Mapper
import com.yourid.movieshowcase.core.model.database.models.ConfigModel
import com.yourid.movieshowcase.core.model.database.models.MovieDetailsModel
import com.yourid.movieshowcase.core.model.database.models.MovieImageModel
import com.yourid.movieshowcase.core.model.database.models.MoviesResultModel
import com.yourid.movieshowcase.core.model.datasource.Result
import com.yourid.movieshowcase.core.model.network.models.*
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

interface NetworkProvider<T> {

    suspend fun get(args: ArgumentsWrapper? = null): Result<T>?
}

abstract class BaseRemoteProvider<RESPONSE, MODEL>(
    private val mapper: Mapper<RESPONSE, MODEL>
) :
    NetworkProvider<MODEL> {

    abstract suspend fun getResponse(args: ArgumentsWrapper?): Response<RESPONSE>

    override suspend fun get(args: ArgumentsWrapper?): Result<MODEL> {
        return try {
            val result = parseResponse(getResponse(args))
            Result.Success(result)
        } catch (e: UnknownHostException) {
            Result.Error(e)
        } catch (e: ConnectException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun parseResponse(response: Response<RESPONSE>): MODEL {
        val data: RESPONSE?

        if (response.isSuccessful)
            data = response.body()
        else
            throw getNetworkException(response)

        if (data != null)
            return mapData(data)
        else
            throw NetworkException(statusMessage = "Generated Network Error Something went wrong")
    }

    private fun getNetworkException(response: Response<RESPONSE>): NetworkException {
        val type = object : TypeToken<NetworkException>() {}.type
        return Gson().fromJson(response.errorBody()!!.charStream(), type)
    }

    private fun mapData(source: RESPONSE): MODEL = mapper.mapFrom(source)
}

class ConfigsRemoteProvider(
    mapper: Mapper<ConfigResponse, ConfigModel>,
    private val api: ApiInterface
) : BaseRemoteProvider<ConfigResponse, ConfigModel>(mapper) {

    override suspend fun getResponse(args: ArgumentsWrapper?): Response<ConfigResponse> =
        api.getConfigsAsync().await()
}

class NowPlayingMoviesRemoteProvider(
    mapper: Mapper<MoviesResultResponse, MoviesResultModel>,
    private val api: ApiInterface
) : BaseRemoteProvider<MoviesResultResponse, MoviesResultModel>(mapper) {

    override suspend fun getResponse(args: ArgumentsWrapper?): Response<MoviesResultResponse> =
        api.getNowPlayingMoviesAsync().await()
}

class PopularMoviesRemoteProvider(
    mapper: Mapper<MoviesResultResponse, MoviesResultModel>,
    private val api: ApiInterface
) : BaseRemoteProvider<MoviesResultResponse, MoviesResultModel>(mapper) {

    override suspend fun getResponse(args: ArgumentsWrapper?): Response<MoviesResultResponse> =
        api.getPopularMoviesAsync().await()
}

class TopRatedMoviesRemoteProvider(
    mapper: Mapper<MoviesResultResponse, MoviesResultModel>,
    private val api: ApiInterface
) : BaseRemoteProvider<MoviesResultResponse, MoviesResultModel>(mapper) {

    override suspend fun getResponse(args: ArgumentsWrapper?): Response<MoviesResultResponse> =
        api.getTopRatedMoviesAsync().await()
}

class UpcomingMoviesRemoteProvider(
    mapper: Mapper<MoviesResultResponse, MoviesResultModel>,
    private val api: ApiInterface
) : BaseRemoteProvider<MoviesResultResponse, MoviesResultModel>(mapper) {

    override suspend fun getResponse(args: ArgumentsWrapper?): Response<MoviesResultResponse> =
        api.getUpcomingMoviesAsync().await()
}

class MoviesDetailsProvider(
    mapper: Mapper<MovieDetailsResponse, MovieDetailsModel>,
    private val api: ApiInterface
) : BaseRemoteProvider<MovieDetailsResponse, MovieDetailsModel>(mapper){

    override suspend fun getResponse(args: ArgumentsWrapper?): Response<MovieDetailsResponse> {
        val movieId = if (args is MovieIdArgs) args.movieId
        else throw IllegalArgumentException("Invalid object type received")

        return api.getMovieByIdAsync(movieId).await()
    }
}

class MoviesImagesProvider(
    mapper: Mapper<MovieImageResponse, MovieImageModel>,
    private val api: ApiInterface
) : BaseRemoteProvider<MovieImageResponse, MovieImageModel>(mapper){

    override suspend fun getResponse(args: ArgumentsWrapper?): Response<MovieImageResponse> {
        val movieId = if (args is MovieIdArgs) args.movieId
        else throw IllegalArgumentException("Invalid object type received")

        return api.getMovieImagesAsync(movieId).await()
    }
}

class SearchMovieProvider(
    mapper: Mapper<MoviesResultResponse, MoviesResultModel>,
    private val api: ApiInterface
) : BaseRemoteProvider<MoviesResultResponse, MoviesResultModel>(mapper) {

    override suspend fun getResponse(args: ArgumentsWrapper?): Response<MoviesResultResponse> {
        val query = if (args is SearchQueryArgs) args.query
        else throw IllegalArgumentException("Invalid object type received")

        return api.searchMovieAsync(query).await()
    }
}