package com.furkanaskin.app.podpocket.ui.player

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding
import com.furkanaskin.app.podpocket.service.response.Episodes
import com.furkanaskin.app.podpocket.service.response.EpisodesItem
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerActivity : BaseActivity<PlayerViewModel, ActivityPlayerBinding>(PlayerViewModel::class.java) {
    lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    val disposable = CompositeDisposable()


    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getParcelableExtra<EpisodesItem>("pod")
        viewModel.item.set(item)

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

    fun setAudio(audio: Episodes) {
        binding.textViewAlbumName.text = audio.podcast?.title
        binding.textViewTrackName.text = audio.title
        binding.textViewEpisodeName.text = audio.pubDateMs.toString()
        binding.seekBarPlayer.max = audio.audioLength!!

        Log.v("qqq", audio.podcast?.id.toString())

        mediaPlayer = MediaPlayer.create(this, Uri.parse(audio.audio))


        imageViewPlayPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                binding.imageViewPlayPauseButton.setImageResource(R.drawable.ic_play)
                mediaPlayer.pause()
            } else {
                binding.imageViewPlayPauseButton.setImageResource(R.drawable.pause)
                mediaPlayer.start()
                updateSeekBar(audio)
            }
        }

        binding.seekBarPlayer.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                binding.seekBarPlayer.progress=p1
//                if(p2){
//                    mediaPlayer.seekTo(p1)
//                }
            }

        })

    }

    fun updateSeekBar(audio: Episodes) {
        binding.seekBarPlayer.max = audio.audioLength ?: 0

        runnable = Runnable {
            binding.seekBarPlayer.progress = mediaPlayer.currentSeconds

            binding.textViewLeftMinute.text = mediaPlayer.currentSeconds.toString()
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            binding.textViewRightMinute.text = diff.toString()

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }


    // Extension property to get media player current position in seconds
    val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / 1000
        }

    override fun onBackPressed() {
        mediaPlayer.pause()
        super.onBackPressed()
    }
}



