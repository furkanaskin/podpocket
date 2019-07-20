package com.furkanaskin.app.podpocket.ui.podcast

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Furkan on 29.04.2019
 */

class PodcastViewModel(app: Application) : BaseViewModel(app) {

    var podcast: ObservableField<Podcasts> = ObservableField()
    private val disposable = CompositeDisposable()
    var progressLiveData = MutableLiveData<Boolean>()
    val podcastLiveData = MutableLiveData<Resource<Podcasts>>()


    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getEpisodes(id: String) {
        disposable.add(baseApi.getPodcastById(id)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            podcastLiveData.postValue(it)
                            podcast.set(it.data)
                        }
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}