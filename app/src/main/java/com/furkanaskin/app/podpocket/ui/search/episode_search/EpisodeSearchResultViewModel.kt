package com.furkanaskin.app.podpocket.ui.search.episode_search

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.ResultsItem

class EpisodeSearchResultViewModel : BaseViewModel() {
    var item = ObservableField<ResultsItem>()
}