package com.yourid.movieshowcase.ui.moviedetails.viewholders

import android.view.View
import com.yourid.movieshowcase.core.common.ApiConfigLinkProvider
import com.yourid.movieshowcase.core.common.ConfigsHolder
import com.yourid.movieshowcase.core.common.ImageLoader
import com.yourid.movieshowcase.core.model.database.models.ImageModel
import com.yourid.movieshowcase.ui.recycler.BaseViewHolder
import com.yourid.movieshowcase.ui.recycler.GenericAdapter
import kotlinx.android.synthetic.main.item_movie_image.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieImagesVH(private val rootView: View) : BaseViewHolder<ImageModel>(rootView),
    KoinComponent {

    private val imageLoader: ImageLoader by inject()
    private val configs = ConfigsHolder.getConfig()

    override fun bind(data: ImageModel, listener: GenericAdapter.OnItemClickListener<ImageModel>?) {
        setImage(data)
        rootView.setOnClickListener { listener?.onClickItem(data) }
    }

    private fun setImage(data: ImageModel) {
        val linkProvider = ApiConfigLinkProvider(data.filePath, configs)
        imageLoader.loadPoster(linkProvider, rootView.movie_photo)
    }
}