package com.yourid.movieshowcase.core.model.network.mappers

import com.yourid.movieshowcase.core.common.Mapper
import com.yourid.movieshowcase.core.model.database.models.ConfigModel
import com.yourid.movieshowcase.core.model.database.models.ImagesConfigModel
import com.yourid.movieshowcase.core.model.network.models.ConfigResponse
import com.yourid.movieshowcase.core.model.network.models.ImagesConfigResponse

class ConfigsMapper(private val imageConfigMapper: Mapper<ImagesConfigResponse, ImagesConfigModel>) :
    Mapper<ConfigResponse, ConfigModel> {

    override fun mapFrom(source: ConfigResponse): ConfigModel =
        ConfigModel(
            imagesConfig = imageConfigMapper.mapFrom(source.images),
            changeKeys = source.changeKeys
        )
}

class ImageConfigMapper : Mapper<ImagesConfigResponse, ImagesConfigModel> {

    override fun mapFrom(source: ImagesConfigResponse): ImagesConfigModel =
        ImagesConfigModel(
            baseUrl = source.baseUrl,
            secureBaseUrl = source.secureBaseUrl,
            posterSizes = source.posterSizes,
            backdropSizes = source.backdropSizes,
            logoSizes = source.logoSizes,
            stillSizes = source.stillSizes,
            profileSizes = source.profileSizes
        )
}