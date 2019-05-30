package com.furkanaskin.app.podpocket.ui.player

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.Episode
import io.reactivex.Observable
import java.util.*

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerViewModel(app: Application) : BaseViewModel(app) {

    var progressBarView: ObservableField<Boolean> = ObservableField(false)
    val item: ObservableField<Episode> = ObservableField()
    val isFavorite: ObservableField<Boolean> = ObservableField()

    init {
        (app as? Podpocket)?.component!!.inject(this)
    }


    fun getEpisodeDetails(id: String): Observable<Episode> {
        return baseApi.getEpisodeById(id)
    }

    fun stringForTime(timeMs: Int): String {
        val mFormatBuilder = StringBuilder()
        val mFormatter: Formatter
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000

        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600

        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }
}