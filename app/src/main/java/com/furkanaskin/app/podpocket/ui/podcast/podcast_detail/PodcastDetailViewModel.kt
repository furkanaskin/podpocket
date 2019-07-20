package com.furkanaskin.app.podpocket.ui.podcast.podcast_detail

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Furkan on 2019-07-06
 */

class PodcastDetailViewModel(app: Application) : BaseViewModel(app) {

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    var podcast: ObservableField<Podcasts> = ObservableField()
    private val disposable = CompositeDisposable()
    val podcastRecommendationsLiveData = MutableLiveData<Resource<PodcastRecommendations>>()
    var progressLiveData = MutableLiveData<Boolean>()


    fun getPodcastRecommendations(podcastId: String, explicitContent: Int) {
        disposable.add(baseApi.getPodcastRecommendations(podcastId, explicitContent)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> podcastRecommendationsLiveData.postValue(it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                })
    }

}