package com.furkanaskin.app.podpocket.ui.feed

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel

/**
 * Created by Furkan on 16.04.2019
 */

class FeedViewModel(app: Application) : BaseViewModel(app) {

    var item: ObservableField<String> = ObservableField("")

    init {
        (app as? Podpocket)?.component?.inject(this)
    }
}