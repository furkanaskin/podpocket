package com.furkanaskin.app.podpocket.ui.player

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.PlayerEntity
import com.furkanaskin.app.podpocket.db.entities.RecentlyPlaysEntity
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.furkanaskin.app.podpocket.service.response.Episode
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {

    val item: ObservableField<Episode> = ObservableField()
    val isFavorite: ObservableField<Boolean> = ObservableField()
    private val _episodeDetailLiveData = MutableLiveData<Resource<Episode>>()
    val episodeDetailLiveData: LiveData<Resource<Episode>> get() = _episodeDetailLiveData

    fun getEpisodeDetails(id: String) {
        baseApi?.let { baseApi ->
            baseApi.getEpisodeById(id)
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
                                it.data?.isPlaying = true
                                val currentEpisode = it
                                item.set(currentEpisode.data)
                                _episodeDetailLiveData.postValue(currentEpisode)

                                doAsync {

                                    // Set isSelected true for current episode
                                    val episodeEntity = episodeDetailLiveData.value?.data?.id?.let {
                                        db?.episodesDao()?.getEpisode(it)
                                    }
                                    episodeEntity?.isSelected = true
                                    db?.episodesDao()?.insertEpisode(episodeEntity)
                                }
                            }
                            Status.LOADING -> ""
                            Status.ERROR -> Timber.e(it.error)
                        }
                    }
        }
    }

    fun saveCurrentPlayerValues() {
        doAsync {
            val player = PlayerEntity(id = 0,
                    episodeId = episodeDetailLiveData.value?.data?.id,
                    episodeTitle = episodeDetailLiveData.value?.data?.title,
                    podcastTitle = episodeDetailLiveData.value?.data?.podcast?.title,
                    podcastId = episodeDetailLiveData.value?.data?.podcast?.id,
                    explicitContent = episodeDetailLiveData.value?.data?.explicitContent ?: false,
                    audio = episodeDetailLiveData.value?.data?.audio,
                    isPlaying = true)

            db?.playerDao()?.insertPlayer(player)
        }
    }

    fun setRecentlyPlayed(isSelected: Boolean) {
        doAsync {
            val playingEpisodeEntity = db?.episodesDao()?.getPlayingEpisode()
            playingEpisodeEntity?.forEachIndexed { _, episode ->
                episode.isSelected = isSelected
                db?.episodesDao()?.insertEpisode(episode)
            }
        }
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

    fun saveRecentlyPlayed() {
        doAsync {
            user?.lastPlayedPodcast = episodeDetailLiveData.value?.data?.podcast?.id
            user?.lastPlayedEpisode = episodeDetailLiveData.value?.data?.id
            user?.let { db?.userDao()?.updateUser(it) }
        }

        doAsync {
            val recentlyPlayedPodcast: RecentlyPlaysEntity? = episodeDetailLiveData.value?.data?.podcast?.let { RecentlyPlaysEntity(it) }
            val recentlyPlayedEpisode: RecentlyPlaysEntity? = episodeDetailLiveData.value?.data?.let { RecentlyPlaysEntity(it) }

            recentlyPlayedEpisode?.let { db?.recentlyPlaysDao()?.insertRecentlyPlay(it) }
            recentlyPlayedPodcast?.let { db?.recentlyPlaysDao()?.insertRecentlyPlay(it) }
        }
    }
}