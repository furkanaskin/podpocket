package com.furkanaskin.app.podpocket.ui.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.databinding.FragmentPlayerQueueBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.db.entities.PlayerEntity
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.player.queue.QueueAdapter
import com.furkanaskin.app.podpocket.utils.PaginationScrollListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * Created by Furkan on 6.05.2019
 */

class PlayerQueueFragment : BaseFragment<PlayerQueueViewModel, FragmentPlayerQueueBinding>(PlayerQueueViewModel::class.java) {

    var player: PlayerEntity? = null
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var nextEpisodePubDate: Long? = null
    var totalEpisodes: Int? = null

    override fun getLayoutRes(): Int = R.layout.fragment_player_queue

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    companion object {
        fun newInstance(podcastId: String, currentEpisode: Int, totalEpisodes: Int): Fragment {
            val playerQueueFragment = PlayerQueueFragment()
            val bundle = Bundle()
            bundle.putString(Constants.BundleArguments.PODCAST_ID, podcastId)
            bundle.putInt(Constants.BundleArguments.CURRENT_EPISODE, currentEpisode)
            bundle.putInt(Constants.BundleArguments.TOTAL_EPISODES, totalEpisodes)
            playerQueueFragment.arguments = bundle
            return playerQueueFragment
        }
    }

    override fun init() {

        getQueue()

        val adapter = QueueAdapter { item, position, _ ->

            // Update playerActivity's currentPosition and EpisodeId then getEpisodeDetail
            val playerActivity = (activity as PlayerActivity)

            playerActivity.currentPosition = position
            playerActivity.episodeId = playerActivity.episodes[position]
            playerActivity.getEpisodeDetail(item.id)
        }


        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.recyclerViewQueueEpisodes.layoutManager = layoutManager
        mBinding.recyclerViewQueueEpisodes.itemAnimator = DefaultItemAnimator()
        mBinding.recyclerViewQueueEpisodes.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                if (totalEpisodes ?: 0 > layoutManager.itemCount)
                    getMoreItems(nextEpisodePubDate ?: 0)
            }

        })

        mBinding.recyclerViewQueueEpisodes.adapter = adapter

        if (viewModel.progressLiveData.hasActiveObservers())
            viewModel.progressLiveData.removeObservers(this)

        viewModel.progressLiveData.observe(this, Observer<Boolean> {
            if (it)
                showProgress()
            else
                hideProgress()
        })
    }

    private fun getQueue() {
        totalEpisodes = arguments?.getInt(Constants.BundleArguments.TOTAL_EPISODES)

        if (viewModel.db.episodesDao().getEpisodes().hasActiveObservers())
            viewModel.db.episodesDao().getEpisodes().removeObservers(this@PlayerQueueFragment)

        // Get episodes for queue.
        viewModel.db.episodesDao().getEpisodes().observe(this@PlayerQueueFragment, Observer<List<EpisodeEntity>> { t ->
            nextEpisodePubDate = t?.last()?.pubDateMs
            (mBinding.recyclerViewQueueEpisodes.adapter as QueueAdapter).submitList(t.toMutableList())
        })
    }

    fun getMoreItems(nextEpisodePubDate: Long) {
        val playerActivity = (activity as PlayerActivity)
        viewModel.getEpisodesWithPaging(getPodcastId() ?: "", nextEpisodePubDate)

        viewModel.podcastLiveData.observe(this@PlayerQueueFragment, Observer<Resource<Podcasts>> {
            totalEpisodes = it.data?.totalEpisodes
            this@PlayerQueueFragment.nextEpisodePubDate = it.data?.nextEpisodePubDate

            doAsync {
                it.data?.episodes?.forEachIndexed { _, episode ->
                    val episodesItem = episode?.let { EpisodeEntity(it) }
                    episodesItem?.let {
                        viewModel.db.episodesDao().insertEpisode(it)
                        playerActivity.episodes.add(episode.id ?: "")
                    }
                }

                runOnUiThread {
                    isLoading = false
                }
            }

        })
    }

    private fun getPodcastId(): String? {
        return arguments?.getString(Constants.BundleArguments.PODCAST_ID)
    }
}