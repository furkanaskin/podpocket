package com.furkanaskin.app.podpocket.ui.search

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.furkanaskin.app.podpocket.service.response.Genres
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.service.response.Search
import io.reactivex.Observable
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

    fun getSearchResult(searchText: String, type: String): Observable<Search> {
        return api.fullTextSearch(searchText, type)
    }

    fun getSearchResultWithGenres(searchText: String, type: String, genreIds: String): Observable<Search> {
        return api.fullTextSearchWithGenres(searchText, type, genreIds)
    }

    fun getEpisodes(id: String): Observable<Podcasts> {

        return baseApi.getPodcastById(id)
    }

    fun getGenres(): Observable<Genres> {

        return api.getGenres()
    }
}