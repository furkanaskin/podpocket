package com.furkanaskin.app.podpocket.ui.feed.locale_feed.locale_posts

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.entities.PostEntity

/**
 * Created by Furkan on 2019-06-25
 */

class LocalePostsItemViewModel : BaseViewModel() {

    var item = ObservableField<PostEntity>()
    var position = -1

    fun setModel(item: PostEntity, position: Int) {
        this.item.set(item)
        this.position = position
    }
}