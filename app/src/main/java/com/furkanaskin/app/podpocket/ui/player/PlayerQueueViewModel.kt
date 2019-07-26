package com.furkanaskin.app.podpocket.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Furkan on 14.05.2019
 */

class PlayerQueueViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {

    private val _podcastLiveData = MutableLiveData<Resource<Podcasts>>()
    val podcastLiveData: LiveData<Resource<Podcasts>> get() = _podcastLiveData

    fun getEpisodesWithPaging(id: String, nextEpisodePubDate: Long) {
        baseApi?.let { baseApi ->
            baseApi.getPodcastByIdWithPaging(id, nextEpisodePubDate)
                    .subscribeOn(Schedulers.io())
                    .map { Resource.success(it) }
                    .onErrorReturn { Resource.error(it) }
                    .doOnSubscribe { progressLiveData.postValue(true) }
                    .doOnTerminate { progressLiveData.postValue(false) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDisposable(this)
                    .subscribe {
                        when (it?.status) {
                            Status.SUCCESS -> _podcastLiveData.postValue(it)
                            Status.LOADING -> ""
                            Status.ERROR -> Timber.e(it.error)
                        }
                    }
        }
    }
}