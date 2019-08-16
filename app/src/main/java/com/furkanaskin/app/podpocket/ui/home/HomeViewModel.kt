package com.furkanaskin.app.podpocket.ui.home

import androidx.lifecycle.LiveData
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
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import org.jetbrains.anko.doAsync
import timber.log.Timber

/**
 * Created by Furkan on 16.04.2019
 */

class HomeViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {

    private val _bestPodcastsLiveData = MutableLiveData<Resource<BestPodcasts>>()
    val bestPodcastsLiveData: LiveData<Resource<BestPodcasts>> get() = _bestPodcastsLiveData
    private val _recommendedPodcastsLiveData = MutableLiveData<Resource<PodcastRecommendations>>()
    val recommendedPodcastsLiveData: LiveData<Resource<PodcastRecommendations>> get() = _recommendedPodcastsLiveData
    private val _recommendedEpisodesLiveData = MutableLiveData<Resource<EpisodeRecommendations>>()
    val recommendedEpisodesLiveData: LiveData<Resource<EpisodeRecommendations>> get() = _recommendedEpisodesLiveData
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
                        Status.SUCCESS -> _bestPodcastsLiveData.postValue(it)
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
                        Status.SUCCESS -> _recommendedPodcastsLiveData.postValue(it)
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
                        Status.SUCCESS -> _recommendedEpisodesLiveData.postValue(it)
                        Status.LOADING -> progressLiveData.postValue(true)
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
        }
    }

    @Suppress("UNCHECKED_CAST")
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
                            val ids: ArrayList<String>? =
                                it.data?.episodes?.map { episodes -> episodes?.id } as? ArrayList<String>
                            podcastEpisodeIds.postValue(ids)
                            writeEpisodesToDb(it)
                        }
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
        }
    }

    private fun writeEpisodesToDb(resource: Resource<Podcasts>) {
        doAsync {
            db?.episodesDao()?.deleteAllEpisodes()
            resource.data?.episodes?.forEachIndexed { _, episode ->
                val episodesItem = episode.let { EpisodeEntity(it) }
                episodesItem.let { db?.episodesDao()?.insertEpisode(it) }
            }
        }
    }
}