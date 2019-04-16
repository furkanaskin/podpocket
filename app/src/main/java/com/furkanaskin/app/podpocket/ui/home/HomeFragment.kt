package com.furkanaskin.app.podpocket.ui.home

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentHomeBinding

/**
 * Created by Furkan on 16.04.2019
 */

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(HomeViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_home
}