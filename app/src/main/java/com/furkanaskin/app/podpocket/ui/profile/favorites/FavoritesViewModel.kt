package com.furkanaskin.app.podpocket.ui.profile.favorites

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.Observable

/**
 * Created by Furkan on 2019-05-18
 */

class FavoritesViewModel(app: Application) : BaseViewModel(app) {

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getEpisodes(id: String): Observable<Podcasts> {

        return baseApi.getPodcastById(id)
    }
}