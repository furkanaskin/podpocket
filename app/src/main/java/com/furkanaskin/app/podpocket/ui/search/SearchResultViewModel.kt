package com.furkanaskin.app.podpocket.ui.search

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.ResultsItem
import com.furkanaskin.app.podpocket.service.response.Search

class SearchResultViewModel(app: Application) : BaseViewModel(app) {
    var item = ObservableField<ResultsItem>()

}