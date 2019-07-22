package com.furkanaskin.app.podpocket.ui.profile.favorites

import android.app.Application
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
 * Created by Furkan on 2019-05-18
 */

class FavoritesViewModel(app: Application) : BaseViewModel(app) {

    val episodesLiveData = MutableLiveData<Resource<Podcasts>>()

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getEpisodes(id: String) {
        disposable.add(baseApi.getPodcastById(id)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe{progressLiveData.postValue(true)}
                .doOnTerminate{progressLiveData.postValue(false)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> episodesLiveData.postValue(it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                })
    }
}