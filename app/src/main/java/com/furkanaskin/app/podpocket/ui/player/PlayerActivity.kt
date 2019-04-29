package com.furkanaskin.app.podpocket.ui.player

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding
import com.furkanaskin.app.podpocket.service.response.EpisodesItem
import kotlinx.android.synthetic.main.activity_player.*

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerActivity : BaseActivity<PlayerViewModel, ActivityPlayerBinding>(PlayerViewModel::class.java) {
    lateinit var mediaPlayer: MediaPlayer
    lateinit var podcastTitle: String


    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getParcelableExtra<EpisodesItem>("pod")
        viewModel.item.set(item)
        podcastTitle = intent.getStringExtra("podTitle")
        setAudio(item)

    }

    fun setAudio(audio: EpisodesItem) {
        binding.textViewAlbumName.text = podcastTitle
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