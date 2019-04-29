package com.furkanaskin.app.podpocket.ui.player

import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityPlayerBinding

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerActivity : BaseActivity<PlayerViewModel,ActivityPlayerBinding>(PlayerViewModel::class.java){
    override fun getLayoutRes(): Int = R.layout.activity_player

    override fun initViewModel(viewModel: PlayerViewModel) {
        binding.viewModel = viewModel
    }

}