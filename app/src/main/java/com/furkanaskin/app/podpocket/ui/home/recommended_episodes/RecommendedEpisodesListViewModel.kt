package com.furkanaskin.app.podpocket.ui.home.recommended_episodes

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.RecommendationsItem

/**
 * Created by Furkan on 30.04.2019
 */

class RecommendedEpisodesListViewModel : BaseViewModel() {

    var item = ObservableField<RecommendationsItem>()
}