package com.furkanaskin.app.podpocket.ui.podcast.podcast_detail

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentPodcastDetailBinding

class PodcastDetailFragment : BaseFragment<PodcastDetailViewModel, FragmentPodcastDetailBinding>(PodcastDetailViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_podcast_detail

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }
}
