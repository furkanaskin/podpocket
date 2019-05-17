package com.furkanaskin.app.podpocket.core

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.furkanaskin.app.podpocket.R
import com.google.android.material.card.MaterialCardView
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

    @JvmStatic
    @BindingAdapter("app:explicitContent")
    fun explicitContent(view: MaterialCardView, explicitContent: Boolean) {

        if (explicitContent) {
            view.strokeColor = ContextCompat.getColor(view.context, R.color.explicitContent)
            view.strokeWidth = 8
        }
    }

    @JvmStatic
    @BindingAdapter("app:isPlayingTitle")
    fun isPlayingTitle(view: TextView, isSelected: Boolean) {
        if (isSelected)
            view.setTextColor(ContextCompat.getColor(view.context, R.color.colorCyan))
        else
            view.setTextColor(ContextCompat.getColor(view.context, R.color.white))


    }

    @JvmStatic
    @BindingAdapter("app:setHtmlText")
    fun setHtml(view: TextView, html: String?) {
        html?.let {
            view.text = Html.fromHtml(html)
        }
    }

    @JvmStatic
    @BindingAdapter("app:isPlayingIcon")
    fun isPlayingIcon(view: ImageView, isSelected: Boolean) {
        if (isSelected)
            view.setImageResource(R.drawable.ic_track_disk)
        else
            view.setImageResource(R.drawable.ic_unplayed_episode_disc)

    }
}