package com.furkanaskin.app.podpocket.ui.home.best_podcasts

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.ChannelsItem

/**
 * Created by Furkan on 28.04.2019
 */

class BestPodcastsListItemViewModel(app: Application) : BaseViewModel(app) {
    var item = ObservableField<ChannelsItem>()
}