package com.furkanaskin.app.podpocket.ui.podcast

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.Observable

/**
 * Created by Furkan on 29.04.2019
 */

class PodcastViewModel(app: Application) : BaseViewModel(app) {

    var podcast: ObservableField<Podcasts> = ObservableField()

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getEpisodes(id: String): Observable<Podcasts> {

        return baseApi.getPodcastById(id)
    }
}