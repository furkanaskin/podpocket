package com.furkanaskin.app.podpocket.ui.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.FragmentPlayerQueueBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.db.entities.PlayerEntity
import com.furkanaskin.app.podpocket.ui.player.queue.QueueAdapter

/**
 * Created by Furkan on 6.05.2019
 */

class PlayerQueueFragment : BaseFragment<PlayerQueueViewModel, FragmentPlayerQueueBinding>(PlayerQueueViewModel::class.java) {

    var player: PlayerEntity? = null

    override fun getLayoutRes(): Int = R.layout.fragment_player_queue

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    companion object {
        fun newInstance(podcastId: String, currentEpisode: Int): Fragment {
            val playerQueueFragment = PlayerQueueFragment()
            val bundle = Bundle()
            bundle.putString(Constants.BundleArguments.PODCAST_ID, podcastId)
            bundle.putInt(Constants.BundleArguments.CURRENT_EPISODE, currentEpisode)
            playerQueueFragment.arguments = bundle
            return playerQueueFragment
        }
    }

    override fun init() {

        showProgress()
        val adapter = QueueAdapter { item, position, _ ->

            // Update playerActivity's currentPosition and EpisodeId then getEpisodeDetail

            val playerActivity = (activity as PlayerActivity)

            playerActivity.currentPosition = position
            playerActivity.episodeId = playerActivity.episodes[position]
            playerActivity.getEpisodeDetail(playerActivity.episodeId)
            mBinding.recyclerViewQueueEpisodes.smoothScrollToPosition(position)
        }

        // Add episodes to queue.
        viewModel.db.episodesDao().getEpisodes().observe(this@PlayerQueueFragment, Observer<List<EpisodeEntity>> { t ->
            mBinding.recyclerViewQueueEpisodes.adapter = adapter
            (mBinding.recyclerViewQueueEpisodes.adapter as QueueAdapter).submitList(t)
        })

        hideProgress()

    }
}