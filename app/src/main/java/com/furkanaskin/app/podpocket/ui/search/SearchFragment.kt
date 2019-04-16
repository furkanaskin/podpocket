package com.furkanaskin.app.podpocket.ui.search

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentSearchBinding

/**
 * Created by Furkan on 16.04.2019
 */

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(SearchViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_search
}