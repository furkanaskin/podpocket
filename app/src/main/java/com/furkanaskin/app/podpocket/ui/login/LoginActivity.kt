package com.furkanaskin.app.podpocket.ui.login

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityLoginBinding

/**
 * Created by Furkan on 14.04.2019
 */

class LoginActivity : BaseActivity<LoginViewModel,ActivityLoginBinding>(LoginViewModel::class.java){
    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun initViewModel(viewModel: LoginViewModel) {
        binding.viewModel = viewModel
    }

}