package com.furkanaskin.app.podpocket.ui.search.podcast_search

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.ResultsItem

class PodcastSearchResultViewModel(app: Application) : BaseViewModel(app) {
    var item = ObservableField<ResultsItem>()

}