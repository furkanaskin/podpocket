package com.furkanaskin.app.podpocket.ui.home

import android.content.Intent
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentHomeBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.response.BestPodcasts
import com.furkanaskin.app.podpocket.service.response.EpisodeRecommendations
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.home.best_podcasts.BestPodcastsAdapter
import com.furkanaskin.app.podpocket.ui.home.recommended_episodes.RecommendedEpisodesAdapter
import com.furkanaskin.app.podpocket.ui.home.recommended_podcasts.RecommendedPodcastsAdapter
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 16.04.2019
 */

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(HomeViewModel::class.java) {
    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    private val disposable = CompositeDisposable()
    var ids: ArrayList<String> = ArrayList()


    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun init() {


        initBestPodcastsAdapter()
        initBestPodcasts()

        initRecommendedPodcastsAdapter()
        initRecommendedPodcasts()

        initRecommendedEpisodesAdapter()
        initRecommendedEpisodes()
    }


    private fun initBestPodcastsAdapter() {
        val adapter = BestPodcastsAdapter { item ->

            val podcastId = item.id
            val action = HomeFragmentDirections.actionHomeFragmentToPodcastEpisodesFragment()
            action.podcastID = podcastId ?: ""
            findNavController().navigate(action)
        }

        mBinding.recyclerViewBestPodcasts.adapter = adapter
        mBinding.recyclerViewBestPodcasts.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)

    }

    private fun initRecommendedPodcastsAdapter() {
        val adapter = RecommendedPodcastsAdapter { item ->
            val podcastId = item.id
            val action = HomeFragmentDirections.actionHomeFragmentToPodcastEpisodesFragment()
            action.podcastID = podcastId ?: ""
            findNavController().navigate(action)
        }

        mBinding.recyclerViewRecommendedPodcasts.adapter = adapter
        mBinding.recyclerViewRecommendedPodcasts.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)

    }

    private fun initRecommendedEpisodesAdapter() {
        val adapter = RecommendedEpisodesAdapter { item ->

            disposable.add(viewModel.getEpisodes(item.podcastId ?: "").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {

                        override fun onSuccess(t: Podcasts) {
                            t.episodes?.forEachIndexed { _, episodesItem ->
                                ids.add(episodesItem?.id ?: "")
                            }

                            doAsync {
                                viewModel.db.episodesDao().deleteAllEpisodes()
                                t.episodes?.forEachIndexed { _, episode ->
                                    val episodesItem = episode.let { EpisodeEntity(it!!) }
                                    episodesItem.let { viewModel.db.episodesDao().insertEpisode(it) }
                                }
                            }
                        }

                        override fun onComplete() {
                            super.onComplete()

                            val intent = Intent(activity, PlayerActivity::class.java)
                            intent.putStringArrayListExtra("allPodIds", ids)
                            intent.putExtra("position", item.id)
                            startActivity(intent)

                        }

                    }))

        }

        mBinding.recyclerViewRecommendedEpisodes.adapter = adapter
    }

    private fun initBestPodcasts() {

        disposable.add(viewModel.getBestPodcasts("tr", 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<BestPodcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: BestPodcasts) {
                        (mBinding.recyclerViewBestPodcasts.adapter as BestPodcastsAdapter).submitList(t.channels)

                    }

                    override fun onError(e: Throwable) {

                    }

                }))

    }

    private fun initRecommendedPodcasts() {

        disposable.add(viewModel.getPodcastRecommendations(user?.lastPlayedPodcast
                ?: "1c8374ef2e8c41928010347f66401e56", 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<PodcastRecommendations>(viewModel.getApplication()) {
                    override fun onSuccess(t: PodcastRecommendations) {
                        (mBinding.recyclerViewRecommendedPodcasts.adapter as RecommendedPodcastsAdapter).submitList(t.recommendations)
                    }

                }))
    }

    private fun initRecommendedEpisodes() {
        disposable.add(viewModel.getEpisodeRecommendations(user?.lastPlayedEpisode
                ?: "53fd8c1a373b46888638cbeb14b571d1", 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<EpisodeRecommendations>(viewModel.getApplication()) {
                    override fun onSuccess(t: EpisodeRecommendations) {
                        (mBinding.recyclerViewRecommendedEpisodes.adapter as RecommendedEpisodesAdapter).submitList(t.recommendations)
                    }

                }))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}