package com.furkanaskin.app.podpocket.ui.player

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.Observable

/**
 * Created by Furkan on 14.05.2019
 */

class PlayerQueueViewModel(app: Application) : BaseViewModel(app) {


    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getEpisodes(id: String): Observable<Podcasts> {

        return baseApi.getPodcastById(id)
    }
}