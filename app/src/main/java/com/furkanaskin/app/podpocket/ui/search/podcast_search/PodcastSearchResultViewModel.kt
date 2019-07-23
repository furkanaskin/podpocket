package com.furkanaskin.app.podpocket.ui.search.podcast_search

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.ResultsItem

class PodcastSearchResultViewModel : BaseViewModel() {
    var item = ObservableField<ResultsItem>()
}