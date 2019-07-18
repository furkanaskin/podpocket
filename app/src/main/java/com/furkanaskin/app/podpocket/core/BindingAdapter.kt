package com.furkanaskin.app.podpocket.core

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

/**
 * Created by Furkan on 15.04.2019
 */
object BindingAdapter {
    @JvmStatic
    @BindingAdapter("app:visibility")
    fun setVisibilty(view: View, isVisible: Boolean) {
        view.hide()
        if (isVisible) {
            view.show()
        } else {
            view.hide()
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
    @BindingAdapter("app:setProfilePicture")
    fun setProfilePicture(view: ImageView, userUniqueId: String?) {
        if (userUniqueId.isNullOrEmpty()) {
            return
        } else {
            val usersRef = FirebaseDatabase.getInstance().getReference("users").child("$userUniqueId")
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val userFromFirebase = snapshot.getValue(UserEntity::class.java)

                    try {
                        Picasso.get().cancelRequest(view)
                        Picasso.get().load(userFromFirebase?.profilePictureUrl).into(view)
                        usersRef.removeEventListener(this)
                    } catch (e: IllegalArgumentException) {
                        usersRef.removeEventListener(this)
                        view.setImageResource(R.drawable.ic_dummy_user)
                    }
                }
            })
        }
    }

    @JvmStatic
    @BindingAdapter("app:setUserName")
    fun setUserName(view: TextView, userUniqueId: String?) {
        if (userUniqueId.isNullOrEmpty()) {
            return
        } else {
            val usersRef = FirebaseDatabase.getInstance().getReference("users").child("$userUniqueId")
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val userFromFirebase = snapshot.getValue(UserEntity::class.java)

                    try {
                        view.text = userFromFirebase?.userName
                        usersRef.removeEventListener(this)
                    } catch (e: IllegalArgumentException) {
                        usersRef.removeEventListener(this)
                        view.text = "ERROR!"
                    }
                }
            })
        }
    }

    @JvmStatic
    @BindingAdapter("app:setDrawable")
    fun setDrawable(view: ImageView, drawable: Int) {
        view.setImageResource(drawable)
    }

    @JvmStatic
    @BindingAdapter("app:explicitContentBorder")
    fun explicitContentBorder(view: ImageView, explicitContent: Boolean) {
        if (explicitContent) {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.bg_explicit_content))
        } else
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.bg_explicit_content_false))
    }

    @JvmStatic
    @BindingAdapter("app:explicitContentToolbar")
    fun explicitContentToolbar(view: Toolbar, explicitContent: Boolean) {
        if (explicitContent) {
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.explicitContent))
        } else
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorCyan))
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