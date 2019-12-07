package com.yourid.movieshowcase.core.model.datasource

import com.yourid.movieshowcase.core.model.cache.CacheState
import com.yourid.movieshowcase.core.model.database.models.MovieModel
import com.yourid.movieshowcase.core.model.database.models.MoviesResultModel
import com.yourid.movieshowcase.core.model.network.ArgumentsWrapper
import com.yourid.movieshowcase.core.model.network.NetworkProvider

class SearchMovieDataSource(private val networkProvider: NetworkProvider<MoviesResultModel>) :
    BaseDataSource<List<MovieModel>>() {

    override suspend fun getCachedState(id: Int): CacheState<List<MovieModel>> = CacheState.Empty()

    override suspend fun getFromRemote(args: ArgumentsWrapper?): Result<List<MovieModel>> {
        return when (val response = networkProvider.get(args)) {
            is Result.Success -> return Result.Success(response.data.results)
            is Result.Error -> return response
            else -> Result.Error(Exception("Something was wrong"))
        }
    }
}