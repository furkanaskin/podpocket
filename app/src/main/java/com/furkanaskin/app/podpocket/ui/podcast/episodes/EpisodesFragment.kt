package com.furkanaskin.app.podpocket.ui.podcast.episodes

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.databinding.FragmentEpisodesBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.utils.PaginationScrollListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.ArrayList

class EpisodesFragment : BaseFragment<EpisodesViewModel, FragmentEpisodesBinding>(EpisodesViewModel::class.java) {

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var nextEpisodePubDate: Long? = null
    var totalEpisodes: Int? = null

    override fun getLayoutRes(): Int = R.layout.fragment_episodes

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    companion object {
        fun getInstance(podcast: Podcasts): EpisodesFragment {
            val bundle = Bundle()
            bundle.putParcelable(Constants.IntentName.PODCAST_ITEM, podcast)

            val fragment = EpisodesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun init() {
        super.init()

        parseIntent()

        val adapter = EpisodesAdapter { item, position ->

            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_ITEM, item)
            intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION, position.toString())
            intent.putStringArrayListExtra(Constants.IntentName.PLAYER_ACTIVITY_ALL_IDS, viewModel.ids as ArrayList<String>?)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.recyclerViewPodcastEpisodes.layoutManager = layoutManager
        mBinding.recyclerViewPodcastEpisodes.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
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
        mBinding.recyclerViewPodcastEpisodes.adapter = adapter

        if (viewModel.progressLiveData.hasActiveObservers())
            viewModel.progressLiveData.removeObservers(this)

        viewModel.progressLiveData.observe(
            this,
            Observer<Boolean> {
                if (it)
                    showProgress()
                else
                    hideProgress()
            }
        )
    }

    fun getMoreItems(nextEpisodePubDate: Long) {
        viewModel.getEpisodesWithPaging(viewModel.podcast.get()?.id ?: "", nextEpisodePubDate)

        if (viewModel.podcastLiveData.hasActiveObservers())
            viewModel.podcastLiveData.removeObservers(this)

        viewModel.podcastLiveData.observe(
            this,
            Observer<Resource<Podcasts>> {
                totalEpisodes = it.data?.totalEpisodes
                this@EpisodesFragment.nextEpisodePubDate = it.data?.nextEpisodePubDate

                doAsync {
                    it.data?.episodes?.forEachIndexed { _, episode ->
                        val episodesItem = episode?.let { EpisodeEntity(it) }
                        episodesItem?.let {
                            viewModel.db?.episodesDao()?.insertEpisode(it)
                            viewModel.ids.add(episode.id ?: "")
                        }
                    }

                    runOnUiThread {
                        isLoading = false
                        viewModel.db?.episodesDao()?.getEpisodes()?.removeObservers(this@EpisodesFragment)
                        viewModel.db?.episodesDao()?.getEpisodes()?.observe(
                            this@EpisodesFragment,
                            Observer<List<EpisodeEntity>> { t ->
                                (mBinding.recyclerViewPodcastEpisodes.adapter as EpisodesAdapter).submitList(t)
                            }
                        )
                    }
                }
            }
        )
    }

    private fun parseIntent() {
        if (arguments != null) {

            viewModel.podcast.set(arguments?.getParcelable(Constants.IntentName.PODCAST_ITEM))
            nextEpisodePubDate = viewModel.podcast.get()?.nextEpisodePubDate

            doAsync {
                viewModel.podcast.get()?.episodes?.forEachIndexed { _, episode ->
                    val episodesItem = episode?.let { EpisodeEntity(it) }
                    episodesItem?.let {
                        viewModel.db?.episodesDao()?.insertEpisode(it)
                        viewModel.ids.add(episode.id ?: "")
                    }
                }
            }

            viewModel.db?.episodesDao()?.getEpisodes()?.removeObservers(this@EpisodesFragment)
            viewModel.db?.episodesDao()?.getEpisodes()?.observe(
                this@EpisodesFragment,
                Observer<List<EpisodeEntity>> { t ->
                    (mBinding.recyclerViewPodcastEpisodes.adapter as EpisodesAdapter).submitList(t)
                }
            )
        }
    }
}
