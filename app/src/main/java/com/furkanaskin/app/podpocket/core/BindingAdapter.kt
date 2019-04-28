package com.furkanaskin.app.podpocket.core

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

/**
 * Created by Furkan on 15.04.2019
 */
object BindingAdapter {
    @JvmStatic
    @BindingAdapter("app:visibility")
    fun setVisibilty(view: View, isVisible: Boolean) {
        view.visibility = View.GONE
        if (isVisible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("app:setDrawableLink")
    fun setDrawableLink(view: ImageView, link: String?) {
        if (link.isNullOrEmpty())
            return
        Picasso.get().cancelRequest(view)
        Picasso.get().load(link).into(view)
    }
}