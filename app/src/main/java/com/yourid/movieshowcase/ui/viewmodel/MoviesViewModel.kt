package com.yourid.movieshowcase.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yourid.movieshowcase.core.model.database.models.*
import com.yourid.movieshowcase.core.model.database.models.CATEGORY.*
import com.yourid.movieshowcase.core.model.datasource.DataSource
import com.yourid.movieshowcase.core.model.datasource.Result
import com.yourid.movieshowcase.core.model.network.MovieIdArgs
import com.yourid.movieshowcase.core.model.network.SearchQueryArgs
import com.yourid.movieshowcase.core.model.network.models.NetworkException
import kotlinx.coroutines.launch

private const val TAG = "MoviesViewModel"

class MoviesViewModel(
    private val movieDataSource: DataSource<List<MovieModel>>,
    private val movieDetailsDataSource: DataSource<MovieDetailsModel>,
    private val searchMovieDataSource: DataSource<List<MovieModel>>,
    private val movieImagesDataSource: DataSource<MovieImageModel>
) : BaseViewModel() {

    val nowPlayingMoviesLiveData = MutableLiveData<MutableList<MovieModel>>()
    val popularMoviesLiveData = MutableLiveData<MutableList<MovieModel>>()
    val topRatedMovieLiveData = MutableLiveData<MutableList<MovieModel>>()
    val upcomingMoviesLiveData = MutableLiveData<MutableList<MovieModel>>()
    val movieDetailsLiveData = MutableLiveData<MutableList<MovieDetailsModel>>()
    val searchMovieLiveData = MutableLiveData<MutableList<MovieModel>>()
    val movieImagesLiveData = MutableLiveData<MutableList<ImageModel>>()

    fun getMovies() {
        showLoader()
        backgroundScope.launch {
            getAllMovies()
            hideLoader()
        }
    }

    fun getMovieDetails(id: Int) {
        showLoader()
        backgroundScope.launch {
            when (val result = movieDetailsDataSource.get(MovieIdArgs(id))) {
                is Result.Success -> movieDetailsLiveData.postValue(mutableListOf(result.data))
                is Result.Error -> handleError(result.exception) { getMovieDetails(id) }
            }
            hideLoader()
        }
    }

    fun searchMovie(query: String) {
        backgroundScope.launch {
            when (val result = searchMovieDataSource.get((SearchQueryArgs(query)))) {
                is Result.Success -> searchMovieLiveData.postValue(result.data.toMutableList())
                is Result.Error -> handleError(result.exception) { searchMovie(query) }
            }
        }
    }

    fun getMovieImages(id: Int) {
        backgroundScope.launch {
            when (val result = movieImagesDataSource.get(MovieIdArgs(id))) {
                is Result.Success -> movieImagesLiveData.postValue(mapImages(result.data))
                is Result.Error -> handleError(result.exception) { getMovieImages(id) }
            }
        }
    }

    private suspend fun getAllMovies() {
        when (val result = movieDataSource.get()) {
            is Result.Success -> updateUi(result)
            is Result.Error -> handleError(result.exception) { getMovies() }
        }
    }

    private fun updateUi(result: Result.Success<List<MovieModel>>) {
        val nowPlaying = filterByCategory(result, NOW_PLAYING)
        val popular = filterByCategory(result, POPULAR)
        val topRated = filterByCategory(result, TOP_RATED)
        val upcoming = filterByCategory(result, UPCOMING)

        nowPlayingMoviesLiveData.postValue(nowPlaying)
        popularMoviesLiveData.postValue(popular)
        topRatedMovieLiveData.postValue(topRated)
        upcomingMoviesLiveData.postValue(upcoming)
    }

    private fun filterByCategory(result: Result.Success<List<MovieModel>>, category: CATEGORY) =
        result.data.filter { it.category.contains(category) }.toMutableList()

    private fun mapImages(imageModel: MovieImageModel): MutableList<ImageModel>? =
        mutableListOf<ImageModel>().apply {
            addAll(imageModel.backdrops)
            addAll(imageModel.posters)
        }

    private fun handleError(error: Exception, func: () -> Unit) {
        if (error is NetworkException) {
            showError(error.statusMessage, func)
            Log.d(TAG, error.statusMessage)
        } else
            showError(error.message ?: "Something was wrong", func)
    }
}