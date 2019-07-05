package com.furkanaskin.app.podpocket.ui.podcast.episodes

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.Observable

class EpisodesViewModel(app: Application) : BaseViewModel(app) {

    var podcast: ObservableField<Podcasts> = ObservableField()
    val ids: ArrayList<String> = ArrayList()

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getEpisodesWithPaging(id: String, nextEpisodePubDate: Long): Observable<Podcasts> {

        return baseApi.getPodcastByIdWithPaging(id, nextEpisodePubDate)
    }
}
