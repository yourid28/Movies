package com.yourid.movieshowcase.core.model.datasource

import com.yourid.movieshowcase.core.model.cache.CacheState
import com.yourid.movieshowcase.core.model.cache.DataBaseSourceCacheProvider
import com.yourid.movieshowcase.core.model.database.models.MovieDetailsModel
import com.yourid.movieshowcase.core.model.network.ArgumentsWrapper
import com.yourid.movieshowcase.core.model.network.MovieIdArgs
import com.yourid.movieshowcase.core.model.network.NetworkProvider

class MovieDetailsDataSource(
    private val networkProvider: NetworkProvider<MovieDetailsModel>,
    private val cacheProvider: DataBaseSourceCacheProvider<MovieDetailsModel>
) : BaseDataSource<MovieDetailsModel>() {

    override suspend fun getCachedState(id: Int): CacheState<MovieDetailsModel> =
        cacheProvider.getById(id)

    override fun getCachedData(cacheState: CacheState.Actual<MovieDetailsModel>): Result<MovieDetailsModel> =
        Result.Success(cacheState.data)

    override suspend fun getFromRemote(args: ArgumentsWrapper?): Result<MovieDetailsModel> {
        return when (val response = networkProvider.get(args)) {
            is Result.Success -> {
                putToCache(response.data)
                return response
            }
            is Result.Error -> return response
            else -> Result.Error(Exception("Something was wrong"))
        }
    }

    override suspend fun putToCache(model: MovieDetailsModel) {
        cacheProvider.put(model)
    }

    override fun getId(args: ArgumentsWrapper?): Int {
        return if (args is MovieIdArgs)
            args.movieId
        else
            throw IllegalArgumentException("Invalid object type received")
    }
}