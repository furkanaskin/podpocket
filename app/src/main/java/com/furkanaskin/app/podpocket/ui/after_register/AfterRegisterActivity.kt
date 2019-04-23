package com.furkanaskin.app.podpocket.ui.after_register

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityAfterRegisterBinding

/**
 * Created by Furkan on 21.04.2019
 */

class AfterRegisterActivity : BaseActivity<AfterRegisterViewModel, ActivityAfterRegisterBinding>(AfterRegisterViewModel::class.java) {
    override fun getLayoutRes() = R.layout.activity_after_register

    override fun initViewModel(viewModel: AfterRegisterViewModel) {
        binding.viewModel = viewModel
    }

}