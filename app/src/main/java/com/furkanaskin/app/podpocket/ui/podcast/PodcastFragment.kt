package com.furkanaskin.app.podpocket.ui.podcast

import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.databinding.FragmentPodcastBinding
import com.furkanaskin.app.podpocket.service.response.Podcasts
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 29.04.2019
 */

class PodcastFragment : BaseFragment<PodcastViewModel, FragmentPodcastBinding>(PodcastViewModel::class.java) {

    private val podcastFragmentArgs: PodcastFragmentArgs by navArgs()

    override fun getLayoutRes(): Int = R.layout.fragment_podcast

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun init() {
        super.init()
        // Delete previous episodes.
        doAsync {
            viewModel.db?.episodesDao()?.deleteAllEpisodes()
        }

        if (!isStateSaved)
            getData()

        if (viewModel.progressLiveData.hasActiveObservers())
            viewModel.progressLiveData.removeObservers(this)

        viewModel.progressLiveData.observe(
            viewLifecycleOwner,
            Observer<Boolean> {
                if (it)
                    showProgress()
                else
                    hideProgress()
            }
        )
    }

    fun getData() {
        viewModel.getEpisodes(podcastFragmentArgs.podcastID)

        if (viewModel.podcastLiveData.hasActiveObservers())
            viewModel.podcastLiveData.removeObservers(this)

        viewModel.podcastLiveData.observe(
            this@PodcastFragment,
            Observer<Resource<Podcasts>> {
                initAdapter()
            }
        )
    }

    private fun initAdapter() {
        childFragmentManager.let { fragmentManager ->

            viewModel.podcast.get()?.let { podcasts ->
                PodcastFragmentPagerAdapter(context, fragmentManager, podcasts)
                    .also { mBinding.viewPager.adapter = it }
            }
        }

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }
}