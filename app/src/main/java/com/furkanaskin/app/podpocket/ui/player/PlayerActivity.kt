package com.furkanaskin.app.podpocket.ui.player

import android.os.Bundle
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding
import com.furkanaskin.app.podpocket.service.response.EpisodesItem

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerActivity : BaseActivity<PlayerViewModel, ActivityPlayerBinding>(PlayerViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getParcelableExtra<EpisodesItem>("pod")
        binding.textViewAlbumName.text = item?.title

    }

}