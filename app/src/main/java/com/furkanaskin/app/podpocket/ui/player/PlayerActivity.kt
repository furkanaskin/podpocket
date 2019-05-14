package com.furkanaskin.app.podpocket.ui.player

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding
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
    private lateinit var episodes: ArrayList<String>

    private val trackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory()
    private val trackSelector: TrackSelector = DefaultTrackSelector(trackSelectionFactory)

    private var handler: Handler = Handler()
    private val disposable = CompositeDisposable()
    private var currentPosition: Int = 0

    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentPosition = intent.getStringExtra("position").toInt()
        episodes = intent.getStringArrayListExtra("allPodIds")
        getEpisodeDetail(episodes[currentPosition])


        binding.imageButtonQueue.setOnClickListener {
            fragmentLayoutQueue.visibility = View.VISIBLE
            imageButtonCloseQueue.visibility = View.VISIBLE
            imageButtonQueue.visibility = View.GONE
            imageViewTrackDisk.visibility = View.GONE
            textViewTrackName.visibility = View.GONE

            val playerQueueFragment = PlayerQueueFragment.newInstance(viewModel.item.get()?.podcast?.id
                    ?: "", currentPosition)
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentLayoutQueue, playerQueueFragment, "playerQueueFragment")
                    .commitNow()

        }

        binding.imageButtonCloseQueue.setOnClickListener {
            textViewTrackName.visibility = View.VISIBLE
            imageViewTrackDisk.visibility = View.VISIBLE
            imageButtonQueue.visibility = View.VISIBLE
            imageButtonCloseQueue.visibility = View.GONE

            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            val queueFragment = supportFragmentManager.findFragmentByTag("playerQueueFragment")

            queueFragment?.let { it -> transaction.remove(it) }

            fragmentLayoutQueue.visibility = View.GONE

        }

    }

    private fun getEpisodeDetail(episodeId: String) {

        dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoPlayerSample"), BANDWIDTH_METER)


        disposable.add(viewModel.getEpisodeDetails(episodeId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Episode>(viewModel.getApplication()) {
                    override fun onSuccess(t: Episode) {
                        viewModel.item.set(t)
                    }

                    override fun onComplete() {
                        super.onComplete()

                        viewModel.item.get()?.let { setEpisode(it) }
                        viewModel.item.get()?.let { setEpisodeInfo(it) }
                        nextEpisode()
                        previousEpisode()

                    }

                }))
    }

    private fun setEpisode(episode: Episode) {
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
    }

    private fun setEpisodeInfo(episode: Episode) {
        binding.textViewTrackName.text = episode.title
        binding.textViewEpisodeName.text = episode.pubDateMs.toString()
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
        //for next episode
        imageViewNextButton.setOnClickListener {
            if (currentPosition != 0) {

                player.stop()
                getEpisodeDetail(episodes.get(currentPosition - 1))
                currentPosition -= 1
            } else {
                Toast.makeText(this, "Yeni bölüm bulunmamaktadır.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun previousEpisode() {
        imageViewPreviousButton.setOnClickListener {
            if (currentPosition + 1 != episodes.size) {
                player.stop()
                getEpisodeDetail(episodes[currentPosition + 1])
                currentPosition += 1

            } else {
                Toast.makeText(this, "İlk bölümdesiniz.", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun initDefaultTimeBar() {
        defaultTimeBar.requestFocus()
        defaultTimeBar.addListener(timeBarListener)
        defaultTimeBar.onFocusChangeListener = object : SeekBar.OnSeekBarChangeListener, View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (!p2) {
                    return
                }

                player.seekTo(p1 * 1000L)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
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
        super.onDestroy()
    }
}




