package com.furkanaskin.app.podpocket.ui.home

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.service.response.ChannelsItem
import com.furkanaskin.app.podpocket.service.response.EpisodeRecommendations
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Furkan on 16.04.2019
 */

class HomeViewModel(app: Application) : BaseViewModel(app) {

    private val disposable = CompositeDisposable()
    var bestPodcastsList: List<ChannelsItem?>? = null
    var forceInitBestPodcasts: ObservableField<Boolean> = ObservableField(false)


    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getBestPodcasts(region: String, explicitContent: Int) {
        disposable.add(baseApi.getBestPodcasts(region, explicitContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { Resource.error(it) }
                .map { Resource.success(it) }
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> Log.v("BestPodcasts", "Success and data :${it.data?.data.toString()}")
                        Status.LOADING -> Log.v("BestPodcasts", "Loading")
                        Status.ERROR -> Log.v("BestPodcasts", "${it.error?.printStackTrace()}")
                        null -> ""
                    }
                })
    }

    fun getPodcastRecommendations(podcastId: String, explicitContent: Int): Observable<PodcastRecommendations> {
        return baseApi.getPodcastRecommendations(podcastId, explicitContent)
    }

    fun getEpisodeRecommendations(podcastId: String, explicitContent: Int): Observable<EpisodeRecommendations> {
        return baseApi.getEpisodeRecommendations(podcastId, explicitContent)
    }

    fun getEpisodes(id: String): Observable<Podcasts> {
        return baseApi.getPodcastById(id)
    }
}