package com.furkanaskin.app.podpocket.ui.home

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.databinding.FragmentHomeBinding
import com.furkanaskin.app.podpocket.service.response.BestPodcasts
import com.furkanaskin.app.podpocket.service.response.EpisodeRecommendations
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.home.best_podcasts.BestPodcastsAdapter
import com.furkanaskin.app.podpocket.ui.home.recommended_episodes.RecommendedEpisodesAdapter
import com.furkanaskin.app.podpocket.ui.home.recommended_podcasts.RecommendedPodcastsAdapter
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show

/**
 * Created by Furkan on 16.04.2019
 */

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(HomeViewModel::class.java) {

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun init() {

        /* if (!isStateSaved) {
                Fetch data
         }else{
               Don't take any action.
         }*/

        initBestPodcastsAdapter()
        initBestPodcasts()

        initRecommendedPodcastsAdapter()
        initRecommendedPodcasts()

        initRecommendedEpisodesAdapter()
        initRecommendedEpisodes()

        hideTitles()

        mBinding.buttonSearch.setOnClickListener {
            navigate(R.id.action_homeFragment_to_searchFragment)
            val search = (activity as? DashboardActivity)?.binding?.bottomNavigation?.menu?.getItem(1)
            (activity as? DashboardActivity)?.binding?.bottomNavigation?.selectedItemId = search?.itemId!!
        }

        mBinding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        if (viewModel.progressLiveData.hasActiveObservers())
            viewModel.progressLiveData.removeObservers(this)

        viewModel.progressLiveData.observe(
            this@HomeFragment,
            Observer<Boolean> {
                if (it)
                    showProgress()
                else
                    hideProgress()
            }
        )
    }

    private fun refreshData() {
        initBestPodcasts()
        initRecommendedPodcasts()
        initRecommendedEpisodes()
        mBinding.swipeRefreshLayout.isRefreshing = false
    }

    private fun initBestPodcastsAdapter() {
        val adapter = BestPodcastsAdapter { item ->

            val podcastId = item.id
            val action = HomeFragmentDirections.actionHomeFragmentToPodcastFragment().setPodcastID(podcastId ?: "")
            findNavController().navigate(action)
        }

        mBinding.recyclerViewBestPodcasts.adapter = adapter
        mBinding.recyclerViewBestPodcasts.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
    }

    private fun initRecommendedPodcastsAdapter() {

        val adapter = RecommendedPodcastsAdapter { item ->
            val podcastId = item.id
            val action = HomeFragmentDirections.actionHomeFragmentToPodcastFragment().setPodcastID(podcastId ?: "")
            findNavController().navigate(action)
        }

        mBinding.recyclerViewRecommendedPodcasts.adapter = adapter
        mBinding.recyclerViewRecommendedPodcasts.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
    }

    private fun initRecommendedEpisodesAdapter() {
        val adapter = RecommendedEpisodesAdapter { item ->

            viewModel.getEpisodes(item.podcastId ?: "")

            if (viewModel.podcastEpisodeIds.hasActiveObservers())
                viewModel.podcastEpisodeIds.removeObservers(this)

            viewModel.podcastEpisodeIds.observe(
                this@HomeFragment,
                Observer<ArrayList<String>> {
                    val intent = Intent(activity, PlayerActivity::class.java)
                    intent.putStringArrayListExtra(Constants.IntentName.PLAYER_ACTIVITY_ALL_IDS, it)
                    intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION, item.id)
                    startActivity(intent)
                }
            )
        }

        mBinding.recyclerViewRecommendedEpisodes.adapter = adapter
    }

    private fun initBestPodcasts() {

        viewModel.getBestPodcasts(viewModel.currentLocation, 0)

        if (viewModel.bestPodcastsLiveData.hasActiveObservers())
            viewModel.bestPodcastsLiveData.removeObservers(this)

        viewModel.bestPodcastsLiveData.observe(
            this@HomeFragment,
            Observer<Resource<BestPodcasts>> {
                showTitles()
                (mBinding.recyclerViewBestPodcasts.adapter as? BestPodcastsAdapter)?.submitList(it.data?.podcasts)
            }
        )
    }

    private fun initRecommendedPodcasts() {

        viewModel.getPodcastRecommendations(
            viewModel.user?.lastPlayedPodcast
                ?: "1c8374ef2e8c41928010347f66401e56",
            0
        )

        if (viewModel.recommendedPodcastsLiveData.hasActiveObservers())
            viewModel.recommendedPodcastsLiveData.removeObservers(this)

        viewModel.recommendedPodcastsLiveData.observe(
            this@HomeFragment,
            Observer<Resource<PodcastRecommendations>> {
                showTitles()
                (mBinding.recyclerViewRecommendedPodcasts.adapter as? RecommendedPodcastsAdapter)?.submitList(it.data?.recommendations)
            }
        )
    }

    private fun initRecommendedEpisodes() {
        viewModel.getEpisodeRecommendations(
            viewModel.user?.lastPlayedEpisode
                ?: "53fd8c1a373b46888638cbeb14b571d1",
            0
        )

        if (viewModel.recommendedEpisodesLiveData.hasActiveObservers())
            viewModel.recommendedEpisodesLiveData.removeObservers(this)

        viewModel.recommendedEpisodesLiveData.observe(
            this@HomeFragment,
            Observer<Resource<EpisodeRecommendations>> {
                showTitles()
                (mBinding.recyclerViewRecommendedEpisodes.adapter as? RecommendedEpisodesAdapter)?.submitList(it.data?.recommendations)
            }
        )
    }

    private fun hideTitles() {
        mBinding.textViewBestPodcasts.hide()
        mBinding.textViewRecommendedEpisodes.hide()
        mBinding.textViewRecommendedPodcasts.hide()
    }

    fun showTitles() {
        mBinding.textViewBestPodcasts.show()
        mBinding.textViewRecommendedEpisodes.show()
        mBinding.textViewRecommendedPodcasts.show()
    }
}