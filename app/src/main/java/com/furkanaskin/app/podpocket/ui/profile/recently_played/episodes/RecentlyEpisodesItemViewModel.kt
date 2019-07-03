package com.furkanaskin.app.podpocket.ui.profile.recently_played.episodes

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.furkanaskin.app.podpocket.db.entities.RecentlyPlaysEntity

/**
 * Created by Furkan on 2019-07-03
 */

class RecentlyEpisodesItemViewModel : ViewModel() {

    var item: ObservableField<RecentlyPlaysEntity> = ObservableField()
}