package com.yourid.movieshowcase.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yourid.movieshowcase.R
import com.yourid.movieshowcase.ui.main.viewholders.MovieVH
import com.yourid.movieshowcase.ui.main.viewholders.SearchMovieVH
import com.yourid.movieshowcase.ui.moviedetails.viewholders.FullScreenImageVH
import com.yourid.movieshowcase.ui.moviedetails.viewholders.MovieImagesVH

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView),
    GenericAdapter.Binder<T>

object ViewHolderFactory {

    fun create(view: View, viewType: Int): BaseViewHolder<*> =
        when (viewType) {
            R.layout.item_movie -> MovieVH(view)
            R.layout.item_search_movie -> SearchMovieVH(view)
            R.layout.item_movie_image -> MovieImagesVH(view)
            R.layout.item_fullscreen_image -> FullScreenImageVH(view)
            else -> throw Exception("Wrong view type")
        }
}