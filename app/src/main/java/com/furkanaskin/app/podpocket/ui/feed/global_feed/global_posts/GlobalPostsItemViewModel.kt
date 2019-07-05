package com.furkanaskin.app.podpocket.ui.feed.global_feed.global_posts

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.entities.PostEntity

/**
 * Created by Furkan on 2019-05-31
 */

class GlobalPostsItemViewModel(app: Application) : BaseViewModel(app) {
    var item = ObservableField<PostEntity>()
    var position = -1

    fun setModel(item: PostEntity, position: Int) {
        this.item.set(item)
        this.position = position

    }
}