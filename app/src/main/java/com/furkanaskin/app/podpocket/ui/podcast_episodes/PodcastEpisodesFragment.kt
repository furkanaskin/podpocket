package com.furkanaskin.app.podpocket.ui.podcast_episodes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.FragmentPodcastEpisodesBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.ui.podcast_episodes.episodes.EpisodesAdapter
import com.furkanaskin.app.podpocket.utils.PaginationScrollListener
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import java.util.*

/**
 * Created by Furkan on 29.04.2019
 */

class PodcastEpisodesFragment : BaseFragment<PodcastEpisodesViewModel, FragmentPodcastEpisodesBinding>(PodcastEpisodesViewModel::class.java) {

    val disposable = CompositeDisposable()
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var nextEpisodePubDate: Long? = null

    override fun getLayoutRes(): Int = R.layout.fragment_podcast_episodes

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EpisodesAdapter { item, position ->

            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_ITEM, item)
            intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION, position.toString())
            intent.putStringArrayListExtra(Constants.IntentName.PLAYER_ACTIVITY_ALL_IDS, viewModel.ids as ArrayList<String>?)
            startActivity(intent)

        }


        // Delete previous episodes.

        doAsync {
            viewModel.db.episodesDao().deleteAllEpisodes()
        }

        getData()

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
                getMoreItems(nextEpisodePubDate ?: 0)
            }

        })
        mBinding.recyclerViewPodcastEpisodes.adapter = adapter

    }

    fun getData() {
        showProgress()
        disposable.add(viewModel.getEpisodes(getPodcastId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: Podcasts) {
                        viewModel.podcast.set(t)
                        nextEpisodePubDate = t.nextEpisodePubDate

                        doAsync {
                            viewModel.podcast.get()?.episodes?.forEachIndexed { _, episode ->
                                val episodesItem = episode?.let { EpisodeEntity(it) }
                                episodesItem?.let {
                                    viewModel.db.episodesDao().insertEpisode(it)
                                    viewModel.ids.add(episode.id ?: "")
                                }
                            }
                        }
                    }

                    override fun onComplete() {
                        super.onComplete()
                        hideProgress()
                        viewModel.db.episodesDao().getEpisodes().observe(this@PodcastEpisodesFragment, Observer<List<EpisodeEntity>> { t ->
                            (mBinding.recyclerViewPodcastEpisodes.adapter as EpisodesAdapter).submitList(t)
                        })

                    }

                }))

    }

    fun getMoreItems(nextEpisodePubDate: Long) {
        showProgress()
        disposable.add(viewModel.getEpisodesWithPaging(getPodcastId(), nextEpisodePubDate).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: Podcasts) {
                        viewModel.podcast.set(t)
                        this@PodcastEpisodesFragment.nextEpisodePubDate = t.nextEpisodePubDate

                        doAsync {
                            t.episodes?.forEachIndexed { _, episode ->
                                val episodesItem = episode?.let { EpisodeEntity(it) }
                                episodesItem?.let {
                                    viewModel.db.episodesDao().insertEpisode(it)
                                    viewModel.ids.add(episode.id ?: "")
                                }
                            }
                        }
                    }

                    override fun onComplete() {
                        super.onComplete()
                        hideProgress()
                        isLoading = false
                        viewModel.db.episodesDao().getEpisodes().removeObservers(this@PodcastEpisodesFragment)
                        viewModel.db.episodesDao().getEpisodes().observe(this@PodcastEpisodesFragment, Observer<List<EpisodeEntity>> { t ->
                            (mBinding.recyclerViewPodcastEpisodes.adapter as EpisodesAdapter).submitList(t)
                        })

                    }

                }))

    }

    fun getPodcastId(): String {
        return PodcastEpisodesFragmentArgs.fromBundle(this.arguments!!).podcastID
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}