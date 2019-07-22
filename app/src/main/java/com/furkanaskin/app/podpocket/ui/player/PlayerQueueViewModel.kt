package com.furkanaskin.app.podpocket.ui.player

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Furkan on 14.05.2019
 */

class PlayerQueueViewModel(app: Application) : BaseViewModel(app) {

    private val _podcastLiveData = MutableLiveData<Resource<Podcasts>>()
    val podcastLiveData: LiveData<Resource<Podcasts>> get() = _podcastLiveData

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getEpisodesWithPaging(id: String, nextEpisodePubDate: Long) {

        disposable.add(baseApi.getPodcastByIdWithPaging(id, nextEpisodePubDate)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> _podcastLiveData.postValue(it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                })
    }
}