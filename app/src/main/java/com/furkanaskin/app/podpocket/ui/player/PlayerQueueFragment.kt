package com.furkanaskin.app.podpocket.ui.player

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.FragmentPlayerQueueBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.db.entities.PlayerEntity
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.player.queue.QueueAdapter
import com.furkanaskin.app.podpocket.utils.PaginationScrollListener
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * Created by Furkan on 6.05.2019
 */

class PlayerQueueFragment : BaseFragment<PlayerQueueViewModel, FragmentPlayerQueueBinding>(PlayerQueueViewModel::class.java) {

    var player: PlayerEntity? = null
    private val disposable = CompositeDisposable()
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var nextEpisodePubDate: Long? = null
    var totalEpisodes: Int? = null

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

        val adapter = QueueAdapter { item, position, _ ->

            // Update playerActivity's currentPosition and EpisodeId then getEpisodeDetail

            val playerActivity = (activity as PlayerActivity)

            playerActivity.currentPosition = position
            playerActivity.episodeId = playerActivity.episodes[position]
            playerActivity.getEpisodeDetail(item.id)
            mBinding.recyclerViewQueueEpisodes.adapter?.notifyDataSetChanged()
            mBinding.recyclerViewQueueEpisodes.smoothScrollToPosition(position)
        }

        getData(adapter)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.recyclerViewQueueEpisodes.layoutManager = layoutManager
        mBinding.recyclerViewQueueEpisodes.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                if (totalEpisodes != layoutManager.itemCount)
                    getMoreItems(nextEpisodePubDate ?: 0)
            }

        })
    }

    private fun getData(adapter: QueueAdapter) {
        showProgress()
        // Add episodes to queue.
        var episodes: MutableList<EpisodeEntity?>?

        doAsync {
            episodes = viewModel.db.episodesDao().getEpisodesWithoutLiveData().toMutableList()
            runOnUiThread {
                mBinding.recyclerViewQueueEpisodes.adapter = adapter
                (mBinding.recyclerViewQueueEpisodes.adapter as QueueAdapter).submitList(episodes)
                hideProgress()
            }
        }


    }

    fun getMoreItems(nextEpisodePubDate: Long) {
        showProgress()
        disposable.add(viewModel.getEpisodesWithPaging(getPodcastId()
                ?: "", nextEpisodePubDate).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: Podcasts) {
                        val playerActivity = (activity as PlayerActivity)
                        totalEpisodes = t.totalEpisodes
                        this@PlayerQueueFragment.nextEpisodePubDate = t.nextEpisodePubDate

                        doAsync {
                            t.episodes?.forEachIndexed { _, episode ->
                                val episodesItem = episode?.let { EpisodeEntity(it) }
                                episodesItem?.let {
                                    viewModel.db.episodesDao().insertEpisode(it)
                                    playerActivity.episodes.add(episode.id ?: "")
                                }
                            }
                        }


                    }

                    override fun onComplete() {
                        super.onComplete()
                        isLoading = false
                        viewModel.db.episodesDao().getEpisodes().removeObservers(this@PlayerQueueFragment)
                        viewModel.db.episodesDao().getEpisodes().observe(this@PlayerQueueFragment, Observer<List<EpisodeEntity>> { t ->

                            (mBinding.recyclerViewQueueEpisodes.adapter as QueueAdapter).submitList(t)

                        })
                        hideProgress()

                    }

                }))
    }

    private fun getPodcastId(): String? {
        return arguments?.getString(Constants.BundleArguments.PODCAST_ID)
    }
}