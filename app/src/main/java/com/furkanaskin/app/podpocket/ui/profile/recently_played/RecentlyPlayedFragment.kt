package com.furkanaskin.app.podpocket.ui.profile.recently_played

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentRecentlyPlayedBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.response.Episode
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 17.05.2019
 */

class RecentlyPlayedFragment : BaseFragment<RecentlyPlayedViewModel, FragmentRecentlyPlayedBinding>(RecentlyPlayedViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.fragment_recently_played

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    private val disposable = CompositeDisposable()
    var ids: ArrayList<String> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recentlyPlayedPodcastId = user?.lastPlayedPodcast
        val recentlyPlayedEpisodeId = user?.lastPlayedEpisode

        disposable.add(viewModel.getRecentlyPlayedPodcastDetails(recentlyPlayedPodcastId
                ?: "53fd8c1a373b46888638cbeb14b571d1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: Podcasts) {
                        viewModel.podcastItem.set(t)

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


                }))

        mBinding.cardView.setOnClickListener {
            val podcastId = viewModel.podcastItem.get()?.id
            val action = RecentlyPlayedFragmentDirections.actionRecentlyPlayedFragmentToPodcastEpisodesFragment()
            action.podcastID = podcastId ?: ""
            findNavController().navigate(action)
        }

        mBinding.textViewListenEpisode.setOnClickListener {
            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putStringArrayListExtra("allPodIds", ids)
            intent.putExtra("position", viewModel.episodeItem.get()?.id)
            startActivity(intent)
        }


        disposable.add(viewModel.getRecentlyPlayedEpisodeDetails(recentlyPlayedEpisodeId ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Episode>(viewModel.getApplication()) {
                    override fun onSuccess(t: Episode) {
                        viewModel.episodeItem.set(t)
                    }

                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}