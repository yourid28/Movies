package com.yourid.movieshowcase.ui.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.view_error_layout.*
import kotlinx.android.synthetic.main.view_progress_bar.*

abstract class BaseActivity : AppCompatActivity(), BaseView {

    override fun showLoading() {
        hideError()
        loading_view?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view?.visibility = View.GONE
    }

    override fun showError(errorMessage: String, func: (() -> Unit)?) {
        setRepeatButton(func)
        error_screen.visibility = View.VISIBLE
        error_message_text.text = errorMessage
    }

    override fun hideError() {
        error_screen.visibility = View.GONE
    }

    override fun onBackPressed() {
        hideError()
        super.onBackPressed()
    }

    private fun setRepeatButton(func: (() -> Unit)?) {
        if (func != null) {
            repeat_button.visibility = View.VISIBLE
            repeat_button.setOnClickListener { func.invoke() }
        } else
            repeat_button.visibility = View.GONE
    }
}