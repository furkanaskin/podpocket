package com.furkanaskin.app.podpocket.ui.podcast.podcast_detail

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.databinding.FragmentPodcastDetailBinding
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.home.recommended_podcasts.RecommendedPodcastsAdapter
import com.furkanaskin.app.podpocket.ui.podcast.PodcastFragmentDirections
import java.util.*

class PodcastDetailFragment : BaseFragment<PodcastDetailViewModel, FragmentPodcastDetailBinding>(PodcastDetailViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.fragment_podcast_detail

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    companion object {
        fun getInstance(podcast: Podcasts): PodcastDetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(Constants.IntentName.PODCAST_ITEM, podcast)

            val fragment = PodcastDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun init() {
        super.init()
        parseIntent()
        initRecommendedPodcastsAdapter()
        initRecommendedPodcasts()

        mBinding.imageViewPodcastCountry.countryCode = getCountryCode(viewModel.podcast.get()?.country
                ?: "")

    }

    private fun parseIntent() {
        if (arguments != null) {
            viewModel.podcast.set(arguments?.getParcelable(Constants.IntentName.PODCAST_ITEM))
        }
    }

    private fun getCountryCode(countryName: String) = Locale.getAvailableLocales().find { it.getDisplayCountry(Locale.US) == countryName }?.country?.toLowerCase()

    private fun initRecommendedPodcasts() {

        viewModel.getPodcastRecommendations(viewModel.podcast.get()?.id
                ?: "1c8374ef2e8c41928010347f66401e56", 0)

        if (viewModel.podcastRecommendationsLiveData.hasActiveObservers())
            viewModel.podcastRecommendationsLiveData.removeObservers(this)

        viewModel.podcastRecommendationsLiveData.observe(this@PodcastDetailFragment, Observer<Resource<PodcastRecommendations>> {
            (mBinding.recyclerViewSimilarPodcasts.adapter as RecommendedPodcastsAdapter).submitList(it.data?.recommendations)
        })
    }

    private fun initRecommendedPodcastsAdapter() {

        val adapter = RecommendedPodcastsAdapter { item ->
            val podcastId = item.id
            val action = PodcastFragmentDirections.actionPodcastFragmentSelf()
            action.podcastID = podcastId ?: ""
            findNavController().navigate(action)
        }

        mBinding.recyclerViewSimilarPodcasts.adapter = adapter
        mBinding.recyclerViewSimilarPodcasts.layoutManager = LinearLayoutManager((activity as DashboardActivity), RecyclerView.HORIZONTAL, false)
    }
}
