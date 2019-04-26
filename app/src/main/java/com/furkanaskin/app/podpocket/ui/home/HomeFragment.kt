package com.furkanaskin.app.podpocket.ui.home

import android.os.Bundle
import android.view.View
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Furkan on 16.04.2019
 */

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(HomeViewModel::class.java) {


    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun init() {
        mBinding.viewModel = viewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonLogout.setOnClickListener {
            mAuth.signOut()
        }

    }


}