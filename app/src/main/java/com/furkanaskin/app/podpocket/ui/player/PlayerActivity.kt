package com.furkanaskin.app.podpocket.ui.player

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.databinding.Observable
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding
import com.furkanaskin.app.podpocket.db.entities.FavoriteEpisodeEntity
import com.furkanaskin.app.podpocket.service.response.Episode
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.player_container.*
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerActivity :
    BaseActivity<PlayerViewModel, ActivityPlayerBinding>(PlayerViewModel::class.java) {

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSource: ConcatenatingMediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    private lateinit var httpDataSourceFactory: DefaultHttpDataSourceFactory
    private var isPlaying = false
    lateinit var episodes: ArrayList<String>
    lateinit var episodeId: String

    private val trackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory()
    private val trackSelector: TrackSelector = DefaultTrackSelector(trackSelectionFactory)

    private var handler: Handler = Handler()
    var currentPosition: Int = 0

    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        episodes = intent.getStringArrayListExtra(Constants.IntentName.PLAYER_ACTIVITY_ALL_IDS)

        if (intent.getStringExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION).length > 4) {

            // User coming from homeFragment recommendations or favorites fragment, just try to get episode detail.
            episodeId = intent.getStringExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION)
            getEpisodeDetail(episodeId)
        } else {

            // User coming from Podcast Episodes fragment. First we need the find which position clicked then we can get episode detail.
            currentPosition =
                intent.getStringExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION).toInt()
            episodeId = episodes[currentPosition]
            getEpisodeDetail(episodeId)
        }

        binding.buttonQueue.setOnClickListener {
            fragmentLayoutQueue.show()
            imageButtonCloseQueue.show()
            imageButtonQueue.hide()
            imageViewTrackDisk.hide()
            textViewTrackName.hide()
            relativeLayoutHeader.hide()

            // After making visibility settings we can add Player Queue

            val playerQueueFragment = PlayerQueueFragment.newInstance(
                viewModel.episodeDetailLiveData.value?.data?.podcast?.id
                    ?: "",
                currentPosition,
                viewModel.episodeDetailLiveData.value?.data?.podcast?.totalEpisodes
                    ?: 0
            )
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            val queueFragment = supportFragmentManager.findFragmentByTag("playerQueueFragment")
            if (queueFragment != null) {
                // If is already committed, don't commit again.
            } else {
                transaction.add(
                    R.id.fragmentLayoutQueue,
                    playerQueueFragment,
                    "playerQueueFragment"
                )
                    .commitNow()
            }

            binding.relativeLayoutNowPlaying.hide()
        }

        binding.imageButtonCloseQueue.setOnClickListener {
            textViewTrackName.show()
            imageViewTrackDisk.show()
            imageButtonQueue.show()
            imageButtonCloseQueue.hide()
            relativeLayoutHeader.show()

            // After making visibility settings we can remove Player Queue

            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            val queueFragment = supportFragmentManager.findFragmentByTag("playerQueueFragment")

            queueFragment?.let { it -> transaction.remove(it) }?.commit()

            fragmentLayoutQueue.hide()
            binding.relativeLayoutNowPlaying.show()
        }

        binding.imageButtonFavorite.setOnClickListener {

            // if this episode favorite/not favorite enable/disable imageButton
            // and change viewModel isFavorite boolean too
            // Because we are observing that boolean, when this property changed we are gonna save this episode in Favorites or we are gonna delete.

            if (viewModel.isFavorite.get() == false) {
                viewModel.isFavorite.set(true)
                binding.imageButtonFavorite.setImageResource(R.drawable.ic_favorite_enabled)
            } else {
                viewModel.isFavorite.set(false)
                binding.imageButtonFavorite.setImageResource(R.drawable.ic_favorite_disabled)
            }
        }

        imageViewNextButton.setOnClickListener {
            nextEpisode()
        }

        imageViewPreviousButton.setOnClickListener {
            previousEpisode()
        }

        viewModel.isFavorite.addOnPropertyChangedCallback(object :
                Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    favoriteVisibility()
                    setFavorite()
                }
            })

        if (viewModel.progressLiveData.hasActiveObservers())
            viewModel.progressLiveData.removeObservers(this)

        viewModel.progressLiveData.observe(
            this,
            Observer<Boolean> {
                if (it)
                    showProgress()
                else
                    hideProgress()
            }
        )
    }

    fun favoriteVisibility() {

        // if this episode favorite/not favorite just enable/disable imageButton

        if (viewModel.isFavorite.get() == false) {
            binding.imageButtonFavorite.setImageResource(R.drawable.ic_favorite_disabled)
        } else {
            binding.imageButtonFavorite.setImageResource(R.drawable.ic_favorite_enabled)
        }
    }

    fun setFavorite() {

        // Save if isFavorite true , delete if isFavorite false

        doAsync {
            if (viewModel.isFavorite.get() == true) {
                viewModel.db?.favoritesDao()
                    ?.insertFavoriteEpisode(
                        viewModel.episodeDetailLiveData.value?.data?.let {
                            FavoriteEpisodeEntity(
                                it
                            )
                        }
                    )
            } else {
                viewModel.db?.favoritesDao()?.deleteFavoriteEpisode(episodeId)
            }
        }
    }

    private fun checkFavorite(title: String?) {

        // Check current episode isFavorite true or false

        doAsync {
            val favoriteEpisode = viewModel.db?.favoritesDao()?.getFavoriteEpisode(episodeId)

            runOnUiThread {

                // I used try/catch block because in UI Thread when favoriteEpisode.title comes null, app is crashing.
                // We just wanna learn this episode is fav. or not
                // if it's fav. set true else set false.

                try {
                    if (favoriteEpisode?.title == title)
                        viewModel.isFavorite.set(true)
                    else
                        viewModel.isFavorite.set(false)
                } catch (e: NullPointerException) {
                    viewModel.isFavorite.set(false)
                }
            }
        }
    }

    fun getEpisodeDetail(episodeId: String) {

        // set isSelected false for recently played episode.
        viewModel.setRecentlyPlayed(false)

        // Prepare dataSource

        httpDataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, "Podpocket"),
            null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true
        )
        dataSourceFactory = DefaultDataSourceFactory(this, null, httpDataSourceFactory)

        viewModel.getEpisodeDetails(episodeId)

        if (viewModel.episodeDetailLiveData.hasActiveObservers())
            viewModel.episodeDetailLiveData.removeObservers(this)

        viewModel.episodeDetailLiveData.observe(
            this,
            Observer<Resource<Episode>> {
                // if player already initialized (already playing some episode) release it.
                if (::player.isInitialized) {
                    player.release()
                }

                checkFavorite(it.data?.title)
                it.data?.let { episode -> setEpisode(episode) }
                it.data?.let { episode -> setEpisodeInfo(episode) }
            }
        )
    }

    private fun setEpisode(episode: Episode) {
        val media = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(episode.audio))

        mediaSource = ConcatenatingMediaSource(media)
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        player.addListener(eventListener)
        initDefaultTimeBar()

        with(player) {
            prepare(mediaSource)
            setPlayPause(true)
            imageViewPlayButton.setOnClickListener {
                imageViewPlayButton.setOnClickListener {
                    setPlayPause(!isPlaying)
                }
            }
        }

        // Save current player values to db.
        viewModel.saveCurrentPlayerValues()
    }

    private fun setEpisodeInfo(episode: Episode) {
        binding.textViewTrackName.text = episode.title
        binding.textViewPodcastTitle.text = episode.podcast?.title
    }

    private fun setPlayPause(play: Boolean) {
        isPlaying = play
        player.playWhenReady = play

        if (!isPlaying) {
            imageViewPlayButton.setImageResource(R.drawable.ic_play)
            binding.textViewIsPlaying.setText(R.string.paused)
        } else {
            setProgress()
            imageViewPlayButton.setImageResource(R.drawable.ic_pause)
            binding.textViewIsPlaying.setText(R.string.now_playing)
        }
    }

    private fun setProgress() {
        textViewCurrentTime.text = binding.viewModel?.stringForTime(player.currentPosition.toInt())
        textViewEndTime.text = binding.viewModel?.stringForTime(player.duration.toInt())

        handler.post(object : Runnable {
            override fun run() {
                if (isPlaying) {
                    defaultTimeBar.setDuration((player.duration / 1000))
                    val mCurrentPosition = player.currentPosition.toInt() / 1000
                    defaultTimeBar.setPosition(mCurrentPosition.toLong())
                    textViewCurrentTime.text =
                        binding.viewModel?.stringForTime(player.currentPosition.toInt())
                    textViewEndTime.text = binding.viewModel?.stringForTime(player.duration.toInt())
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun nextEpisode() {
        if (currentPosition != 0) {
            player.stop()
            // Update positions, it's very important for Player/PlayerQueue

            currentPosition -= 1
            episodeId = episodes[currentPosition]
            getEpisodeDetail(episodeId)
            disableButtons()
        } else {
            viewModel.toastLiveData.postValue(getString(R.string.no_more_new_episodes))
        }
    }

    private fun previousEpisode() {
        if (currentPosition + 1 != episodes.size) {
            player.stop()
            // Update positions, it's very important for Player/PlayerQueue

            currentPosition += 1
            episodeId = episodes[currentPosition]
            getEpisodeDetail(episodeId)
            disableButtons()
        } else {
            viewModel.toastLiveData.postValue(getString(R.string.you_are_in_first_episode))
        }
    }

    private fun initDefaultTimeBar() {
        defaultTimeBar.requestFocus()
        defaultTimeBar.addListener(timeBarListener)
        defaultTimeBar.onFocusChangeListener =
            object : SeekBar.OnSeekBarChangeListener, View.OnFocusChangeListener {
                override fun onFocusChange(p0: View?, p1: Boolean) {}

                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (!p2) {
                        return
                    }

                    player.seekTo(p1 * 1000L)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    player.seekTo((p0?.progress ?: 0) * 1000L)
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    player.seekTo((p0?.progress ?: 0) * 1000L)
                }
            }
    }

    private val eventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {

                Player.STATE_ENDED -> {
                    nextEpisode()
                }
            }
        }
    }

    private val timeBarListener = object : TimeBar.OnScrubListener {
        override fun onScrubMove(timeBar: TimeBar?, position: Long) {
            player.seekTo(position * 1000L)
        }

        override fun onScrubStart(timeBar: TimeBar?, position: Long) {
        }

        override fun onScrubStop(timeBar: TimeBar?, position: Long, canceled: Boolean) {
            player.seekTo(position * 1000L)
        }
    }

    override fun onBackPressed() {
        player.stop()
        super.onBackPressed()
    }

    override fun onDestroy() {
        player.stop()
        viewModel.saveRecentlyPlayed()
        super.onDestroy()
    }

    private fun disableButtons() {
        object : CountDownTimer(700, 100) {
            override fun onFinish() {
                imageViewPreviousButton.isEnabled = true
                imageViewNextButton.isEnabled = true
                imageViewPlayButton.isEnabled = true
                this.cancel() // We don't need that countDownTimer any more..
            }

            override fun onTick(millisUntilFinished: Long) {
                imageViewPreviousButton.isEnabled = false
                imageViewNextButton.isEnabled = false
                imageViewPlayButton.isEnabled = false
            }
        }.start()
    }
}