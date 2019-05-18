package com.furkanaskin.app.podpocket.ui.player

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.Observable
import androidx.fragment.app.FragmentTransaction
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding
import com.furkanaskin.app.podpocket.db.entities.FavoriteEpisodeEntity
import com.furkanaskin.app.podpocket.db.entities.PlayerEntity
import com.furkanaskin.app.podpocket.service.response.Episode
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.player_container.*
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerActivity : BaseActivity<PlayerViewModel, ActivityPlayerBinding>(PlayerViewModel::class.java) {
    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSource: ConcatenatingMediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    private val BANDWIDTH_METER = DefaultBandwidthMeter()
    private var isPlaying = false
    lateinit var episodes: ArrayList<String>
    lateinit var episodeId: String

    private val trackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory()
    private val trackSelector: TrackSelector = DefaultTrackSelector(trackSelectionFactory)

    private var handler: Handler = Handler()
    private val disposable = CompositeDisposable()
    var currentPosition: Int = 0

    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        episodes = intent.getStringArrayListExtra("allPodIds")

        if (intent.getStringExtra("position").length > 4) {

            // User coming from homeFragment recommendations, just try to get episode detail.
            episodeId = intent.getStringExtra("position")
            getEpisodeDetail(episodeId)

        } else {

            // User coming from Podcast Episodes fragment. First we need the find whic position clicked then we can get episode detail.
            currentPosition = intent.getStringExtra("position").toInt()
            episodeId = episodes[currentPosition]
            getEpisodeDetail(episodeId)
        }


        binding.imageButtonQueue.setOnClickListener {
            fragmentLayoutQueue.visibility = View.VISIBLE
            imageButtonCloseQueue.visibility = View.VISIBLE
            imageButtonQueue.visibility = View.GONE
            imageViewTrackDisk.visibility = View.GONE
            textViewTrackName.visibility = View.GONE
            relativeLayoutHeader.visibility = View.GONE

            // After making visibility settings we can add Player Queue

            val playerQueueFragment = PlayerQueueFragment.newInstance(viewModel.item.get()?.podcast?.id
                    ?: "", currentPosition)
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentLayoutQueue, playerQueueFragment, "playerQueueFragment")
                    .commitNow()

            binding.relativeLayoutNowPlaying.visibility = View.GONE
        }

        binding.imageButtonCloseQueue.setOnClickListener {
            textViewTrackName.visibility = View.VISIBLE
            imageViewTrackDisk.visibility = View.VISIBLE
            imageButtonQueue.visibility = View.VISIBLE
            imageButtonCloseQueue.visibility = View.GONE
            relativeLayoutHeader.visibility = View.VISIBLE

            // After making visibility settings we can remove Player Queue

            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            val queueFragment = supportFragmentManager.findFragmentByTag("playerQueueFragment")

            queueFragment?.let { it -> transaction.remove(it) }?.commit()

            fragmentLayoutQueue.visibility = View.GONE
            binding.relativeLayoutNowPlaying.visibility = View.VISIBLE

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

        viewModel.isFavorite.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                favoriteVisibility()
                setFavorite()
            }
        })
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
                viewModel.db.favoritesDao().insertFavoriteEpisode(FavoriteEpisodeEntity(viewModel.item.get()!!))
            } else {
                viewModel.db.favoritesDao().deleteFavoriteEpisode(episodeId)
            }
        }
    }

    fun checkFavorite() {

        // Check current episode isFavorite true or false

        doAsync {
            val favoriteEpisode = viewModel.db.favoritesDao().getFavoriteEpisode(episodeId)

            runOnUiThread {

                // I used try/catch block because in UI Thread when favoriteEpisode.title comes null, app is crashing.
                // We just wanna learn this episode is fav. or not
                // if it's fav. set true else set false.

                try {
                    favoriteEpisode.title
                    viewModel.isFavorite.set(true)
                } catch (e: NullPointerException) {
                    viewModel.isFavorite.set(false)
                }
            }
        }
    }

    fun getEpisodeDetail(episodeId: String) {

        // set isSelected false for recently played episode.

        doAsync {
            val playingEpisodeEntity = viewModel.db.episodesDao().getPlayingEpisode()
            playingEpisodeEntity.forEachIndexed { index, episode ->
                episode.isSelected = false
                viewModel.db.episodesDao().insertEpisode(episode)
            }
        }

        // if player already initialized (already playing some episode) release it.

        if (::player.isInitialized) {
            player.release()
        }

        // Prepare dataSource

        dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "Podpocket"), BANDWIDTH_METER)


        disposable.add(viewModel.getEpisodeDetails(episodeId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Episode>(viewModel.getApplication()) {
                    override fun onSuccess(t: Episode) {
                        t.isPlaying = true
                        viewModel.item.set(t)
                    }

                    override fun onComplete() {
                        super.onComplete()

                        doAsync {

                            // Set isSelected true for current episode

                            val episodeEntity = viewModel.item.get()?.id?.let {
                                viewModel.db.episodesDao().getEpisode(it)
                            }
                            episodeEntity?.isSelected = true
                            viewModel.db.episodesDao().insertEpisode(episodeEntity)
                        }

                        checkFavorite()
                        viewModel.item.get()?.let { setEpisode(it) }
                        viewModel.item.get()?.let { setEpisodeInfo(it) }
                        nextEpisode()
                        previousEpisode()

                    }

                }))
    }

    fun setEpisode(episode: Episode) {
        val media = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(episode.audio))

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

        doAsync {
            val player = PlayerEntity(id = 0,
                    episodeId = viewModel.item.get()?.id,
                    episodeTitle = viewModel.item.get()?.title,
                    podcastTitle = viewModel.item.get()?.podcast?.title,
                    podcastId = viewModel.item.get()?.podcast?.id,
                    explicitContent = viewModel.item.get()?.explicitContent ?: false,
                    audio = viewModel.item.get()?.audio,
                    isPlaying = true)

            viewModel.db.playerDao().insertPlayer(player)
        }
    }

    fun setEpisodeInfo(episode: Episode) {
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
            imageViewPlayButton.setImageResource(R.drawable.pause)
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
                    textViewCurrentTime.text = binding.viewModel?.stringForTime(player.currentPosition.toInt())
                    textViewEndTime.text = binding.viewModel?.stringForTime(player.duration.toInt())
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun nextEpisode() {
        imageViewNextButton.setOnClickListener {
            if (currentPosition != 0) {
                player.stop()

                // Update positions, it's very important for Player/PlayerQueue

                currentPosition -= 1
                episodeId = episodes[currentPosition]
                getEpisodeDetail(episodeId)
                disableButtons()

            } else {
                Toast.makeText(this, "Yeni bölüm bulunmamaktadır.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun previousEpisode() {
        imageViewPreviousButton.setOnClickListener {
            if (currentPosition + 1 != episodes.size) {
                player.stop()

                // Update positions, it's very important for Player/PlayerQueue

                currentPosition += 1
                episodeId = episodes[currentPosition]
                getEpisodeDetail(episodeId)
                disableButtons()

            } else {
                Toast.makeText(this, "İlk bölümdesiniz.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initDefaultTimeBar() {
        defaultTimeBar.requestFocus()
        defaultTimeBar.addListener(timeBarListener)
        defaultTimeBar.onFocusChangeListener = object : SeekBar.OnSeekBarChangeListener, View.OnFocusChangeListener {
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

    private val eventListener = object : ExoPlayer.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_ENDED -> {
                    setPlayPause(false)
                    defaultTimeBar.setDuration(player.duration)
                    player.seekTo(0)

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
        doAsync {
            // Save last played podcast and episode to DB
            user?.lastPlayedPodcast = viewModel.item.get()?.podcast?.id
            user?.lastPlayedEpisode = viewModel.item.get()?.id
            user?.let { viewModel.db.userDao().updateUser(it) }
        }

        disposable.clear()
        super.onDestroy()
    }

    private fun disableButtons() {
        object : CountDownTimer(700, 100) {
            override fun onFinish() {
                imageViewPreviousButton.isEnabled = true
                imageViewNextButton.isEnabled = true
                imageViewPlayButton.isEnabled = true
                cancel() // We don't need that countDownTimer any more..
            }

            override fun onTick(millisUntilFinished: Long) {
                imageViewPreviousButton.isEnabled = false
                imageViewNextButton.isEnabled = false
                imageViewPlayButton.isEnabled = false
            }
        }.start()
    }
}