package com.furkanaskin.app.podpocket.ui.player

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding
import com.furkanaskin.app.podpocket.service.response.Episodes
import com.furkanaskin.app.podpocket.service.response.EpisodesItem
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerActivity : BaseActivity<PlayerViewModel, ActivityPlayerBinding>(PlayerViewModel::class.java) {
    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    private val BANDWIDTH_METER = DefaultBandwidthMeter()
    private var isPlaying = false

    private val trackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory()
    private val trackSelector: TrackSelector = DefaultTrackSelector(trackSelectionFactory)

    private var handler: Handler = Handler()
    private val disposable = CompositeDisposable()


    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getParcelableExtra<EpisodesItem>("pod")
        viewModel.item.set(item)
        dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoPlayerSample"), BANDWIDTH_METER)


        disposable.add(viewModel.getEpisodeDetails(item.id ?: "").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Episodes>(viewModel.getApplication()) {
                    override fun onSuccess(t: Episodes) {
                        viewModel.episodeItem.set(t)
                    }

                    override fun onComplete() {
                        super.onComplete()

                        viewModel.episodeItem.get()?.let { setAudio(it) }
                        doAsync {
                            // Save last played podcast and episode to DB
                            user?.lastPlayedPodcast = viewModel.episodeItem.get()?.podcast?.id
                            user?.lastPlayedEpisode = viewModel.episodeItem.get()?.id
                            user?.let { viewModel.db.userDao().updateUser(it) }
                        }

                    }

                }))

    }

    private fun setAudio(audio: Episodes) {
        binding.textViewAlbumName.text = audio.podcast?.title
        binding.textViewTrackName.text = audio.title
        binding.textViewEpisodeName.text = audio.pubDateMs.toString()

        mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(audio.audio))

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        player.addListener(eventListener)
        initDefaultTimeBar()

        mediaPlayer = MediaPlayer.create(this, Uri.parse(audio.audio))

        with(player) {
            prepare(mediaSource)
            binding.imageViewPlayButton.setOnClickListener {
                setPlayPause(!isPlaying)
            }
        }


    }


    private fun setPlayPause(play: Boolean) {
        isPlaying = play
        player.playWhenReady = play

        if (!isPlaying) {
            binding.imageViewPlayButton.setImageResource(R.drawable.ic_play)
        } else {
            setProgress()
            binding.imageViewPlayButton.setImageResource(R.drawable.pause)

        }
    }

    private fun setProgress() {
//        time_current.text = binding.viewModel?.stringForTime(exoPlayer!!.currentPosition.toInt())
//        player_end_time.text = binding.viewModel?.stringForTime(exoPlayer!!.duration.toInt())

        if (handler == null) handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                if (player != null && isPlaying) {
                    binding.defaultTimeBar.setDuration((player.duration / 1000))
                    val mCurrentPosition = player.currentPosition.toInt() / 1000
                    binding.defaultTimeBar.setPosition(mCurrentPosition.toLong())
//                    time_current.text = binding.viewModel?.stringForTime(exoPlayer!!.currentPosition.toInt())
//                    player_end_time.text = binding.viewModel?.stringForTime(exoPlayer!!.duration.toInt())
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun initDefaultTimeBar() {
        binding.defaultTimeBar.requestFocus()
        // binding.defaultTimeBar.addListener(timeBarListener)
        binding.defaultTimeBar.onFocusChangeListener = object : SeekBar.OnSeekBarChangeListener, View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (!p2) {
                    return
                }

                player.seekTo(p1 * 1000L)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                player.seekTo((p0?.progress ?: 0) * 1000L)
            }

        }
        binding.imageViewNextButton.setOnClickListener {
            player.seekTo(700)
            Log.e("buffered", player.bufferedPosition.toString())
            Log.e("buffered", player.currentPosition.toString())
        }

    }

    private val eventListener = object : ExoPlayer.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_ENDED -> {
                    setPlayPause(false)
                    binding.defaultTimeBar.setDuration(player.duration)
                    player.seekTo(0)

                }
            }
        }
    }
//
//    private val  timeBarListener=object:TimeBar.OnScrubListener{
//        override fun onScrubMove(timeBar: TimeBar?, position: Long) {
//        }
//
//        override fun onScrubStart(timeBar: TimeBar?, position: Long) {
//        }
//
//        override fun onScrubStop(timeBar: TimeBar?, position: Long, canceled: Boolean) {
//            player.seekTo(position)
//        }
//
//    }


    override fun onBackPressed() {
        player.stop()
        super.onBackPressed()
    }

    override fun onDestroy() {
        player.stop()
        super.onDestroy()
    }
}



