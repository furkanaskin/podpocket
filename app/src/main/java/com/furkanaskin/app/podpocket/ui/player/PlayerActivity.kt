package com.furkanaskin.app.podpocket.ui.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding
import com.furkanaskin.app.podpocket.service.response.ChannelsItem
import com.furkanaskin.app.podpocket.service.response.EpisodesItem
import com.furkanaskin.app.podpocket.service.response.JustListen
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_player.*

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerActivity : BaseActivity<PlayerViewModel, ActivityPlayerBinding>(PlayerViewModel::class.java) {
    val disposable = CompositeDisposable()
    lateinit var mediaPlayer: MediaPlayer


    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getParcelableExtra<EpisodesItem>("pod")
        setAudio(item)

    }


    fun setAudio(audio: EpisodesItem) {
        binding.textViewAlbumName.text = audio.podcastTitle
        binding.textViewEpisodeName.text=audio.title
        mediaPlayer = MediaPlayer.create(this, Uri.parse(audio.audio))


        imageViewPlayPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                binding.imageViewPlayPauseButton.setImageResource(R.drawable.ic_play)
                mediaPlayer.pause()
            } else {
                binding.imageViewPlayPauseButton.setImageResource(R.drawable.pause)
                mediaPlayer.start()
            }


        }
    }
}