package com.furkanaskin.app.podpocket.ui.podcast_episodes.episodes

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.EpisodesItem

/**
 * Created by Furkan on 29.04.2019
 */

class EpisodesListItemViewModel(app: Application) : BaseViewModel(app) {
    var item = ObservableField<EpisodesItem>()
}