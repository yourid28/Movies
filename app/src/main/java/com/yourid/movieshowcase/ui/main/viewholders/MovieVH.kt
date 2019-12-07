package com.yourid.movieshowcase.ui.main.viewholders

import android.view.View
import com.yourid.movieshowcase.core.common.ApiConfigLinkProvider
import com.yourid.movieshowcase.core.common.ConfigsHolder
import com.yourid.movieshowcase.core.common.ImageLoader
import com.yourid.movieshowcase.core.model.database.models.MovieModel
import com.yourid.movieshowcase.ui.recycler.BaseViewHolder
import com.yourid.movieshowcase.ui.recycler.GenericAdapter
import kotlinx.android.synthetic.main.item_movie.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieVH(private val rootView: View) : BaseViewHolder<MovieModel>(rootView),
    GenericAdapter.Binder<MovieModel>, KoinComponent {

    private val imageLoader: ImageLoader by inject()
    private val configs = ConfigsHolder.getConfig()

    override fun bind(data: MovieModel, listener: GenericAdapter.OnItemClickListener<MovieModel>?) {
        setPoster(data)
        rootView.title_text.text = data.title
        rootView.setOnClickListener { listener?.onClickItem(data) }
    }

    private fun setPoster(data: MovieModel) {
        val linkProvider = ApiConfigLinkProvider(data.posterPath, configs)
        imageLoader.loadPoster(linkProvider, rootView.poster_image)
    }
}