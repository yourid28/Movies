package com.yourid.movieshowcase.ui.main

import android.graphics.Color
import android.os.Bundle
import com.jaeger.library.StatusBarUtil
import com.yourid.movieshowcase.R
import com.yourid.movieshowcase.ui.base.BaseActivity
import com.yourid.movieshowcase.ui.viewmodel.MoviesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {

    private val moviesViewModel: MoviesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.setColor(this, Color.TRANSPARENT)
    }
}
