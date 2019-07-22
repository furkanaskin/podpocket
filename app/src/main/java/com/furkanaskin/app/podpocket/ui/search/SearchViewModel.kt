package com.furkanaskin.app.podpocket.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.furkanaskin.app.podpocket.service.response.Genres
import com.furkanaskin.app.podpocket.service.response.Search
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Furkan on 16.04.2019
 */

class SearchViewModel(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit var api: PodpocketAPI

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    var selectedGenres: ArrayList<Int> = ArrayList()
    val genresLiveData = MutableLiveData<Resource<Genres>>()
    val episodeSearchResultLiveData = MutableLiveData<Resource<Search>>()
    val podcastSearchResultLiveData = MutableLiveData<Resource<Search>>()
    var podcastEpisodeIds = MutableLiveData<ArrayList<String>>()
    var episodeHeadingLiveData = MutableLiveData<Boolean>()
    var podcastHeadingLiveData = MutableLiveData<Boolean>()


    fun getSearchResult(searchText: String, type: String, offset: Int) {
        disposable.add(baseApi.fullTextSearch(searchText, type, offset)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            when (type) {
                                Constants.SearchQuery.EPISODE -> {
                                    if (it.data?.results.isNullOrEmpty())
                                        episodeHeadingLiveData.postValue(false)
                                    else
                                        episodeHeadingLiveData.postValue(true)

                                    episodeSearchResultLiveData.postValue(it)
                                }
                                Constants.SearchQuery.PODCAST -> {
                                    if (it.data?.results.isNullOrEmpty())
                                        podcastHeadingLiveData.postValue(false)
                                    else
                                        podcastHeadingLiveData.postValue(true)

                                    podcastSearchResultLiveData.postValue(it)

                                }
                            }
                        }
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                })
    }

    fun getSearchResultWithGenres(searchText: String, type: String, genreIds: String, offset: Int) {
        disposable.add(baseApi.fullTextSearchWithGenres(searchText, type, genreIds, offset)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            when (type) {
                                Constants.SearchQuery.EPISODE -> {
                                    if (it.data?.results.isNullOrEmpty())
                                        episodeHeadingLiveData.postValue(false)
                                    else
                                        episodeHeadingLiveData.postValue(true)

                                    episodeSearchResultLiveData.postValue(it)
                                }
                                Constants.SearchQuery.PODCAST -> {
                                    if (it.data?.results.isNullOrEmpty())
                                        podcastHeadingLiveData.postValue(false)
                                    else
                                        podcastHeadingLiveData.postValue(true)

                                    podcastSearchResultLiveData.postValue(it)

                                }
                            }
                        }
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                })
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
                            val ids = ArrayList<String>()
                            it.data?.episodes?.forEachIndexed { _, episodesItem ->
                                ids.add(episodesItem?.id ?: "")
                            }

                            podcastEpisodeIds.postValue(ids)

                            doAsync {
                                db.episodesDao().deleteAllEpisodes()
                                it.data?.episodes?.forEachIndexed { _, episode ->
                                    val episodesItem = episode.let { EpisodeEntity(it!!) }
                                    episodesItem.let { db.episodesDao().insertEpisode(it) }
                                }
                            }

                        }
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                })
    }

    fun getGenres() {
        disposable.add(baseApi.getGenres()
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> genresLiveData.postValue(it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                })
    }
}