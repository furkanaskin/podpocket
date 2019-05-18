package com.furkanaskin.app.podpocket.ui.profile.favorites.favorite_episodes

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.entities.FavoriteEpisodeEntity

/**
 * Created by Furkan on 2019-05-18
 */

class FavoriteEpisodesListViewModel(app: Application) : BaseViewModel(app) {

    var item = ObservableField<FavoriteEpisodeEntity>()
    var position = -1

    fun setModel(item: FavoriteEpisodeEntity, position: Int) {
        this.item.set(item)
        this.position = position

    }

}