package com.furkanaskin.app.podpocket.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentHomeBinding
import com.furkanaskin.app.podpocket.service.response.BestPodcasts
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.furkanaskin.app.podpocket.ui.home.best_podcasts.BestPodcastsAdapter
import com.furkanaskin.app.podpocket.ui.home.recommended_podcasts.RecommendedPodcastsAdapter
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Furkan on 16.04.2019
 */

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(HomeViewModel::class.java) {
    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    val disposable = CompositeDisposable()

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun init() {
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonLogout.setOnClickListener {
            mAuth.signOut()
        }

        initBestPodcastsAdapter()
        initBestPodcasts()

        initRecommendedPodcastsAdapter()
        initRecommendedPodcasts()

    }

    fun initBestPodcastsAdapter() {
        val adapter = BestPodcastsAdapter { item ->

            val podcastId = item.id
            val action = HomeFragmentDirections.actionHomeFragmentToPodcastEpisodesFragment()
            action.podcastID = podcastId ?: ""
            findNavController().navigate(action)
        }

        mBinding.recyclerViewBestPodcasts.adapter = adapter
        mBinding.recyclerViewBestPodcasts.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)

    }

    fun initRecommendedPodcastsAdapter() {
        val adapter = RecommendedPodcastsAdapter { item ->
            val podcastId = item.id
            val action = HomeFragmentDirections.actionHomeFragmentToPodcastEpisodesFragment()
            action.podcastID = podcastId ?: ""
            findNavController().navigate(action)
        }

        mBinding.recyclerViewRecommendedPodcasts.adapter = adapter
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

        disposable.add(viewModel.getPodcastRecommendations(user.lastPlayedPodcast
                ?: "a77e4e3f156248ec9252da682bc70d79", 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<PodcastRecommendations>(viewModel.getApplication()) {
                    override fun onSuccess(t: PodcastRecommendations) {
                        Log.v("qqq", t.recommendations.toString())
                        (mBinding.recyclerViewRecommendedPodcasts.adapter as RecommendedPodcastsAdapter).submitList(t.recommendations)
                    }

                }))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}