package com.furkanaskin.app.podpocket.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.furkanaskin.app.podpocket.service.response.Genres
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.service.response.Search
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import org.jetbrains.anko.doAsync
import timber.log.Timber

/**
 * Created by Furkan on 16.04.2019
 */

class SearchViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) :
    BaseViewModel(api, appDatabase) {

    var selectedGenres: ArrayList<Int> = ArrayList()
    private val _genresLiveData = MutableLiveData<Resource<Genres>>()
    val genresLiveData: LiveData<Resource<Genres>> get() = _genresLiveData
    private val _episodeSearchResultLiveData = MutableLiveData<Resource<Search>>()
    val episodeSearchResultLiveData: LiveData<Resource<Search>> get() = _episodeSearchResultLiveData
    private val _podcastSearchResultLiveData = MutableLiveData<Resource<Search>>()
    val podcastSearchResultLiveData: LiveData<Resource<Search>> get() = _podcastSearchResultLiveData
    var podcastEpisodeIds = MutableLiveData<ArrayList<String>>()
    var episodeHeadingLiveData = MutableLiveData<Boolean>()
    var podcastHeadingLiveData = MutableLiveData<Boolean>()

    fun getSearchResult(searchText: String, type: String, offset: Int) {
        baseApi?.let { baseApi ->
            baseApi.fullTextSearch(searchText, type, offset)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> parseType(type, it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
        }
    }

    fun getSearchResultWithGenres(searchText: String, type: String, genreIds: String, offset: Int) {
        baseApi?.let { baseApi ->
            baseApi.fullTextSearchWithGenres(searchText, type, genreIds, offset)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> parseType(type, it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
        }
    }

    private fun parseType(type: String, resource: Resource<Search>) {
        when (type) {
            Constants.SearchQuery.EPISODE -> {
                if (resource.data?.results.isNullOrEmpty())
                    episodeHeadingLiveData.postValue(false)
                else
                    episodeHeadingLiveData.postValue(true)

                _episodeSearchResultLiveData.postValue(resource)
            }

            Constants.SearchQuery.PODCAST -> {
                if (resource.data?.results.isNullOrEmpty())
                    podcastHeadingLiveData.postValue(false)
                else
                    podcastHeadingLiveData.postValue(true)

                _podcastSearchResultLiveData.postValue(resource)
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

    fun getGenres() {
        baseApi?.let { baseApi ->
            baseApi.getGenres()
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> _genresLiveData.postValue(it)
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