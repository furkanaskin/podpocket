package com.furkanaskin.app.podpocket.ui.podcast.episodes

import androidx.databinding.ObservableField
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
import javax.inject.Inject
import timber.log.Timber

class EpisodesViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {

    private val _podcastLiveData = MutableLiveData<Resource<Podcasts>>()
    val podcastLiveData: LiveData<Resource<Podcasts>> get() = _podcastLiveData
    var podcast: ObservableField<Podcasts> = ObservableField()
    val ids: ArrayList<String> = ArrayList()

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
