package com.furkanaskin.app.podpocket.ui.profile.recently_played

import android.content.Intent
import android.view.View
import androidx.navigation.fragment.findNavController
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.FragmentRecentlyPlayedBinding
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.ui.profile.recently_played.episodes.RecentlyEpisodesAdapter
import com.furkanaskin.app.podpocket.ui.profile.recently_played.podcasts.RecentlyPodcastsAdapter
import com.furkanaskin.app.podpocket.utils.extensions.toast
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.*

/**
 * Created by Furkan on 17.05.2019
 */

class RecentlyPlayedFragment : BaseFragment<RecentlyPlayedViewModel, FragmentRecentlyPlayedBinding>(RecentlyPlayedViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.fragment_recently_played

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun init() {
        super.init()

        mBinding.buttonNavigateHome.setOnClickListener {
            navigate(R.id.action_recentlyPlayedFragment_to_homeFragment)
            val home = (activity as DashboardActivity).binding.bottomNavigation.menu.getItem(0)
            (activity as DashboardActivity).binding.bottomNavigation.selectedItemId = home.itemId
        }

        mBinding.recyclerViewRecentlyPlayedEpisodes.adapter = RecentlyEpisodesAdapter {
            var ids: ArrayList<String> = ArrayList()

            ids.add(it.id)

            if (ids.size > 0) {

                val intent = Intent(activity, PlayerActivity::class.java)
                intent.putStringArrayListExtra(Constants.IntentName.PLAYER_ACTIVITY_ALL_IDS, ids)
                intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION, it.id)
                startActivity(intent)
            } else {
                toast("Bir hata meydana geldi.")
            }
        }

        mBinding.recyclerViewRecentlyPlayedPodcast.adapter = RecentlyPodcastsAdapter {
            val podcastId = it.id
            val action = RecentlyPlayedFragmentDirections.actionRecentlyPlayedFragmentToPodcastFragment()
            action.podcastID = podcastId
            findNavController().navigate(action)
        }

        getDataFromDB()
    }

    fun getDataFromDB() {
        doAsync {
            val podcasts = viewModel.db?.recentlyPlaysDao()?.getRecentlyPlayedPodcasts()
            val episodes = viewModel.db?.recentlyPlaysDao()?.getRecentlyPlayedEpisodes()

            runOnUiThread {
                (mBinding.recyclerViewRecentlyPlayedPodcast.adapter as RecentlyPodcastsAdapter).submitList(podcasts)
                (mBinding.recyclerViewRecentlyPlayedEpisodes.adapter as RecentlyEpisodesAdapter).submitList(episodes)

                if (podcasts?.isEmpty() == true && episodes?.isEmpty() == true) {
                    showAnimation()
                } else {
                    hideAnimation()
                }
            }
        }
    }

    private fun showAnimation() {
        mBinding.textViewRecentlyPlayedEpisodesHeading.visibility = View.GONE
        mBinding.textViewRecentlyPlayedPodcastsHeading.visibility = View.GONE
        mBinding.recyclerViewRecentlyPlayedEpisodes.visibility = View.GONE
        mBinding.recyclerViewRecentlyPlayedPodcast.visibility = View.GONE
        mBinding.lottieAnimationView.visibility = View.VISIBLE
        mBinding.buttonNavigateHome.visibility = View.VISIBLE
        mBinding.textViewDummyText.visibility = View.VISIBLE
    }

    private fun hideAnimation() {
        mBinding.textViewRecentlyPlayedEpisodesHeading.visibility = View.VISIBLE
        mBinding.textViewRecentlyPlayedPodcastsHeading.visibility = View.VISIBLE
        mBinding.recyclerViewRecentlyPlayedEpisodes.visibility = View.VISIBLE
        mBinding.recyclerViewRecentlyPlayedPodcast.visibility = View.VISIBLE
        mBinding.lottieAnimationView.visibility = View.GONE
        mBinding.buttonNavigateHome.visibility = View.GONE
        mBinding.textViewDummyText.visibility = View.GONE
    }
}