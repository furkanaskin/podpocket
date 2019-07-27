package com.furkanaskin.app.podpocket.ui.home

import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.furkanaskin.app.podpocket.service.response.BestPodcasts
import com.furkanaskin.app.podpocket.service.response.EpisodeRecommendations
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Furkan on 16.04.2019
 */

class HomeViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {

    val bestPodcastsLiveData = MutableLiveData<Resource<BestPodcasts>>()
    val recommendedPodcastsLiveData = MutableLiveData<Resource<PodcastRecommendations>>()
    val recommendedEpisodesLiveData = MutableLiveData<Resource<EpisodeRecommendations>>()
    var podcastEpisodeIds = MutableLiveData<ArrayList<String>>()

    fun getBestPodcasts(region: String, explicitContent: Int) {
        baseApi?.let { baseApi ->
            baseApi.getBestPodcasts(region, explicitContent)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> bestPodcastsLiveData.postValue(it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
        }
    }

    fun getPodcastRecommendations(podcastId: String, explicitContent: Int) {
        baseApi?.let { baseApi ->
            baseApi.getPodcastRecommendations(podcastId, explicitContent)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> recommendedPodcastsLiveData.postValue(it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
        }
    }

    fun getEpisodeRecommendations(podcastId: String, explicitContent: Int) {
        baseApi?.let { baseApi ->
            baseApi.getEpisodeRecommendations(podcastId, explicitContent)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> recommendedEpisodesLiveData.postValue(it)
                        Status.LOADING -> progressLiveData.postValue(true)
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
        }
    }

    fun getEpisodes(id: String) {
        baseApi?.let { baseApi ->
            baseApi.getPodcastById(id)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            val ids = ArrayList<String>()
                            it.data?.episodes?.forEachIndexed { _, episodesItem ->
                                ids.add(episodesItem?.id ?: "")
                            }

                            podcastEpisodeIds.postValue(ids)

                            doAsync {
                                db?.episodesDao()?.deleteAllEpisodes()
                                it.data?.episodes?.forEachIndexed { _, episode ->
                                    val episodesItem = episode.let { EpisodeEntity(it!!) }
                                    episodesItem.let { db?.episodesDao()?.insertEpisode(it) }
                                }
                            }
                        }
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
        }
    }
}