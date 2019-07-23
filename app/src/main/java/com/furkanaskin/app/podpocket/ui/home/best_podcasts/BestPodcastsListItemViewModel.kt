package com.furkanaskin.app.podpocket.ui.home.best_podcasts

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.Podcasts

/**
 * Created by Furkan on 28.04.2019
 */

class BestPodcastsListItemViewModel : BaseViewModel() {

    var item = ObservableField<Podcasts>()
}