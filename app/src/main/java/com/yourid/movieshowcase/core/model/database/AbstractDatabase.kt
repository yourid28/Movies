package com.yourid.movieshowcase.core.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yourid.movieshowcase.core.model.database.models.*

@Database(
    entities = [
        ConfigModel::class,
        MovieModel::class,
        MovieDetailsModel::class,
        MovieImageModel::class,
        SourcesTimeStamp::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getConfigDao(): ConfigDao
    abstract fun getMoviesOverviewDao(): MoviesDao
    abstract fun getMovieDetailsDao(): MovieDetailsDao
    abstract fun getMovieImagesDao(): MovieImagesDao
}