package com.furkanaskin.app.podpocket.ui.podcast_episodes

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentPodcastEpisodesBinding
import com.furkanaskin.app.podpocket.service.response.EpisodesItem
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.PodPocketItemDecoration
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.ui.podcast_episodes.episodes.EpisodesAdapter
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Furkan on 29.04.2019
 */

class PodcastEpisodesFragment : BaseFragment<PodcastEpisodesViewModel, FragmentPodcastEpisodesBinding>(PodcastEpisodesViewModel::class.java) {
    val disposable = CompositeDisposable()
    val episodesList: MutableList<EpisodesItem?>? = null
    var podcastTitle:String = ""


    override fun getLayoutRes(): Int = R.layout.fragment_podcast_episodes

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = EpisodesAdapter { item ->

            item.podcastTitle=podcastTitle
            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra("pod", item)
            startActivity(intent)

        }

        disposable.add(viewModel.getEpisodes(getPodcastId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: Podcasts) {

                        (mBinding.recyclerViewPodcastEpisodes.adapter as EpisodesAdapter).submitList(t.episodes)
                        podcastTitle= t.title!!

                    }

                }))

        mBinding.recyclerViewPodcastEpisodes.adapter = adapter
        mBinding.recyclerViewPodcastEpisodes.addItemDecoration(PodPocketItemDecoration(30))

    }

    fun getPodcastId(): String {
        return PodcastEpisodesFragmentArgs.fromBundle(this.arguments!!).podcastID
    }

}