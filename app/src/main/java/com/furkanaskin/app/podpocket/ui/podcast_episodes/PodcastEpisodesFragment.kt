package com.furkanaskin.app.podpocket.ui.podcast_episodes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentPodcastEpisodesBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.ui.podcast_episodes.episodes.EpisodesAdapter
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
    lateinit var podcastTitle: String
    private var totalPage: Int? = 0


    override fun getLayoutRes(): Int = R.layout.fragment_podcast_episodes

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EpisodesAdapter { item, position ->

            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra("pod", item)
            intent.putExtra("position", position.toString())
            intent.putStringArrayListExtra("allPodIds", viewModel.getAllIds(position) as ArrayList<String>?)
            startActivity(intent)

        }

        // Delete previous episodes.

        doAsync {
            viewModel.db.episodesDao().deleteAllEpisodes()
        }

        disposable.add(viewModel.getEpisodes(getPodcastId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: Podcasts) {
                        viewModel.podcast.set(t)

                        podcastTitle = t.title!!

                        val remaining = t.totalEpisodes?.rem(10) ?: 0
                        totalPage = if (remaining > 0) {
                            t.totalEpisodes?.div(10)?.plus(1) // +1 for remaining items
                        } else {
                            t.totalEpisodes?.div(10)
                        }

                        doAsync {
                            viewModel.podcast.get()?.episodes?.forEachIndexed { _, episode ->
                                val episodesItem = episode?.let { EpisodeEntity(it) }
                                episodesItem?.let { viewModel.db.episodesDao().insertEpisode(it) }
                            }
                        }
                    }

                    override fun onComplete() {
                        super.onComplete()

                        viewModel.db.episodesDao().getEpisodes().observe(this@PodcastEpisodesFragment, object : Observer<List<EpisodeEntity>> {
                            override fun onChanged(t: List<EpisodeEntity>?) {
                                (mBinding.recyclerViewPodcastEpisodes.adapter as EpisodesAdapter).submitList(t)
                            }
                        })

                    }

                }))

        mBinding.recyclerViewPodcastEpisodes.adapter = adapter

    }

    fun getPodcastId(): String {
        return PodcastEpisodesFragmentArgs.fromBundle(this.arguments!!).podcastID
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}