package com.furkanaskin.app.podpocket.ui.home

import android.content.Intent
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.FragmentHomeBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.response.BestPodcasts
import com.furkanaskin.app.podpocket.service.response.EpisodeRecommendations
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.home.best_podcasts.BestPodcastsAdapter
import com.furkanaskin.app.podpocket.ui.home.recommended_episodes.RecommendedEpisodesAdapter
import com.furkanaskin.app.podpocket.ui.home.recommended_podcasts.RecommendedPodcastsAdapter
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import timber.log.Timber

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


        mBinding.buttonSearch.setOnClickListener {
            navigate(R.id.action_homeFragment_to_searchFragment)
            val search = (activity as DashboardActivity).binding.bottomNavigation.menu.getItem(1)
            (activity as DashboardActivity).binding.bottomNavigation.selectedItemId = search.itemId
        }

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

            showProgress()
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
                            hideProgress()

                            val intent = Intent(activity, PlayerActivity::class.java)
                            intent.putStringArrayListExtra(Constants.IntentName.PLAYER_ACTIVITY_ALL_IDS, ids)
                            intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION, item.id)
                            startActivity(intent)

                        }

                    }))

        }

        mBinding.recyclerViewRecommendedEpisodes.adapter = adapter
    }

    private fun initBestPodcasts() {

        //If block written for just reduce the number of request
        //Current free request limit is 10k
        //One-time data is enough for us

        if (viewModel.forceInitBestPodcasts.get() != true && viewModel.bestPodcastsList.isNullOrEmpty()) {

            showProgress()
            hideTitles()

            disposable.add(viewModel.getBestPodcasts(viewModel.currentLocation, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : CallbackWrapper<BestPodcasts>(viewModel.getApplication()) {
                        override fun onSuccess(t: BestPodcasts) {
                            (mBinding.recyclerViewBestPodcasts.adapter as BestPodcastsAdapter).submitList(t.channels)
                            hideProgress()
                            viewModel.bestPodcastsList = t.channels
                            viewModel.forceInitBestPodcasts.set(true)
                            showTitles()
                        }

                        override fun onError(e: Throwable) {
                            hideProgress()
                        }

                    }))

        } else {
            (mBinding.recyclerViewBestPodcasts.adapter as BestPodcastsAdapter).submitList(viewModel.bestPodcastsList)
            Timber.tag("Force Init").i("Best podcasts force initialized.")
        }
    }

    private fun initRecommendedPodcasts() {

        showProgress()
        hideTitles()

        disposable.add(viewModel.getPodcastRecommendations(user?.lastPlayedPodcast
                ?: "1c8374ef2e8c41928010347f66401e56", 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<PodcastRecommendations>(viewModel.getApplication()) {
                    override fun onSuccess(t: PodcastRecommendations) {
                        (mBinding.recyclerViewRecommendedPodcasts.adapter as RecommendedPodcastsAdapter).submitList(t.recommendations)
                        hideProgress()
                        showTitles()
                    }

                }))
    }

    private fun initRecommendedEpisodes() {

        showProgress()
        hideTitles()

        disposable.add(viewModel.getEpisodeRecommendations(user?.lastPlayedEpisode
                ?: "53fd8c1a373b46888638cbeb14b571d1", 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<EpisodeRecommendations>(viewModel.getApplication()) {
                    override fun onSuccess(t: EpisodeRecommendations) {
                        (mBinding.recyclerViewRecommendedEpisodes.adapter as RecommendedEpisodesAdapter).submitList(t.recommendations)
                        hideProgress()
                        showTitles()
                    }

                }))
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}