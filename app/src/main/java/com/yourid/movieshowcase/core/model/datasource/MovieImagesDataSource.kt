package com.yourid.movieshowcase.core.model.datasource

import com.yourid.movieshowcase.core.model.cache.CacheState
import com.yourid.movieshowcase.core.model.cache.DataBaseSourceCacheProvider
import com.yourid.movieshowcase.core.model.database.models.MovieImageModel
import com.yourid.movieshowcase.core.model.network.ArgumentsWrapper
import com.yourid.movieshowcase.core.model.network.MovieIdArgs
import com.yourid.movieshowcase.core.model.network.NetworkProvider

class MovieImagesDataSource(
    private val networkProvider: NetworkProvider<MovieImageModel>,
    private val cacheProvider: DataBaseSourceCacheProvider<MovieImageModel>
) : BaseDataSource<MovieImageModel>() {

    override suspend fun getCachedState(id: Int): CacheState<MovieImageModel> = cacheProvider.getById(id)

    override fun getCachedData(cacheState: CacheState.Actual<MovieImageModel>): Result<MovieImageModel> =
        Result.Success(cacheState.data)

    override suspend fun getFromRemote(args: ArgumentsWrapper?): Result<MovieImageModel> {
        return when (val response = networkProvider.get(args)) {
            is Result.Success -> {
                putToCache(response.data)
                return response
            }
            is Result.Error -> return response
            else -> Result.Error(Exception("Something was wrong"))
        }
    }

    override suspend fun putToCache(model: MovieImageModel) {
        cacheProvider.put(model)
    }

    override fun getId(args: ArgumentsWrapper?): Int {
        return if (args is MovieIdArgs)
            args.movieId
        else
            throw IllegalArgumentException("Invalid object type received")
    }
}