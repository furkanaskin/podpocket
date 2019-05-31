package com.furkanaskin.app.podpocket.ui.feed.post_detail

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel

/**
 * Created by Furkan on 2019-05-26
 */

class PostDetailViewModel(app: Application) : BaseViewModel(app) {

    init {
        (app as? Podpocket)?.component?.inject(this)
    }
}