package com.yourid.movieshowcase.ui.moviedetails

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.yourid.movieshowcase.R
import com.yourid.movieshowcase.core.common.ApiConfigLinkProvider
import com.yourid.movieshowcase.core.common.ConfigsHolder
import com.yourid.movieshowcase.core.common.ImageLoader
import com.yourid.movieshowcase.core.model.database.models.GenresItemModel
import com.yourid.movieshowcase.core.model.database.models.ImageModel
import com.yourid.movieshowcase.core.model.database.models.MovieDetailsModel
import com.yourid.movieshowcase.ui.base.BaseFragment
import com.yourid.movieshowcase.ui.recycler.GenericAdapter
import com.yourid.movieshowcase.ui.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_movie_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val FIRST_LINE_TEXT_LENGTH = 15

const val MOVIE_ID = "movie_id"

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details),
    GenericAdapter.OnItemClickListener<ImageModel> {

    private val moviesViewModel: MoviesViewModel by sharedViewModel()
    private val imageLoader: ImageLoader by inject()

    private val photoAdapter by lazy { getImageAdapter() }

    override fun initViewModel() {
        var movieId = 0

        if (arguments == null) showError("Error movie loading", null)
        arguments?.let { movieId = it.getInt(MOVIE_ID) }

        dataLoadingObserve()
        movieDetailsObserve(movieId)
        movieImagesObserve(movieId)
    }

    override fun initView(view: View) {
        initRecycler()

        overview_button.setOnClickListener { showOverview() }
        photos_button.setOnClickListener { showPhotos() }
    }

    override fun dataLoadingObserve() {
        moviesViewModel.isDataLoading.observe(this, Observer { isDataLoaded ->
            if (isDataLoaded)
                showLoading()
            else
                hideLoading()
        })
    }

    override fun exceptionObserve() {
        moviesViewModel.exception.observe(this, Observer { error ->
            if (error != null && error.errorMessage.isNotEmpty())
                showError(error.errorMessage, error.func)
        })
    }

    override fun onClickItem(data: ImageModel) {
        navigateTo(R.id.moviePhotoFragment)
    }

    private fun movieDetailsObserve(movieId: Int) {
        moviesViewModel.getMovieDetails(movieId)
        moviesViewModel.movieDetailsLiveData.observe(this, Observer {
            updateUi(it[0])
        })
    }

    private fun movieImagesObserve(movieId: Int) {
        moviesViewModel.getMovieImages(movieId)
        moviesViewModel.movieImagesLiveData.observe(this, Observer {
            photoAdapter.update(it)
        })
    }

    private fun initRecycler() {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        images_recycler.layoutManager = layoutManager
        images_recycler.adapter = photoAdapter
    }

    private fun updateUi(detailsModel: MovieDetailsModel) {
        loadPoster(detailsModel)

        detailsModel.apply {
            score_text.text = voteAverage.toString()
            title_text.text = getWrappedTitleText(title)
            year_text.text = releaseDate
            genres_text.text = getGenresText(genres)
            overview_title_text.text
            overview_text.text = overview
        }

        play_trailer_button.setOnClickListener { showMessage("Play Trailer Clicked") }
        watched_list_button.setOnClickListener { showMessage("Watched button Clicked") }
    }

    private fun loadPoster(detailsModel: MovieDetailsModel) {
        val configs = ConfigsHolder.getConfig()
        val linkProvider = ApiConfigLinkProvider(detailsModel.posterPath, configs)
        imageLoader.loadPoster(linkProvider, poster_image)
    }

    private fun getWrappedTitleText(title: String): String {
        return if (title.length <= FIRST_LINE_TEXT_LENGTH)
            title
        else {
            val firstLine = title.substring(0, FIRST_LINE_TEXT_LENGTH)
            val enterIndex = firstLine.lastIndexOf(" ")
            title.substring(0, enterIndex) + "\n" + title.substring(enterIndex + 1, title.length)
        }
    }

    private fun getGenresText(genres: List<GenresItemModel>): String {
        var genresText = ""
        for (i in genres.indices) {
            genresText += genres[i].name
            if (i != genres.size - 1) genresText += (", ")

        }
        return genresText
    }

    private fun showMessage(message: String) {
        Snackbar.make(poster_image, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun getImageAdapter() = object : GenericAdapter<ImageModel>(this) {
        override fun getLayoutId(position: Int, obj: ImageModel): Int = R.layout.item_movie_image
    }

    private fun showOverview() {
        overview_button.isEnabled = false
        photos_button.isEnabled = true

        overview_layout.visibility = View.VISIBLE
        images_recycler.visibility = View.GONE
    }

    private fun showPhotos() {
        overview_button.isEnabled = true
        photos_button.isEnabled = false

        overview_layout.visibility = View.GONE
        images_recycler.visibility = View.VISIBLE
    }
}
