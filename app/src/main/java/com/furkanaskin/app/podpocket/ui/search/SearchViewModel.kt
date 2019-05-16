package com.furkanaskin.app.podpocket.ui.search

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.furkanaskin.app.podpocket.service.response.Genres
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Furkan on 16.04.2019
 */

class SearchViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit var api: PodpocketAPI

    var progressBarView: ObservableField<Boolean> = ObservableField(false)

    init {
        (app as? Podpocket)?.component?.inject(this)
    }


    fun getGenres(): Observable<Genres> {
        return api.getGenres()
    }


}