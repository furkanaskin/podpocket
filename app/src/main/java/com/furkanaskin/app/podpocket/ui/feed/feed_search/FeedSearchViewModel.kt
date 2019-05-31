package com.furkanaskin.app.podpocket.ui.feed.feed_search

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel

/**
 * Created by Furkan on 2019-05-26
 */

class FeedSearchViewModel(app: Application) : BaseViewModel(app) {

    init {
        (app as? Podpocket)?.component?.inject(this)
    }
}