package com.furkanaskin.app.podpocket.ui.profile.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentFavoritesBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.db.entities.FavoriteEpisodeEntity
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.ui.profile.favorites.favorite_episodes.FavoriteEpisodesAdapter
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 2019-05-18
 */

class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>(FavoritesViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_favorites

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    private val disposable = CompositeDisposable()
    var ids: ArrayList<String> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressBarView.set(true)

        val adapter = FavoriteEpisodesAdapter { item, position ->

            // Get podcastId and collect all episode Ids then navigate PlayerActivity

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

        viewModel.db.favoritesDao().getFavoriteEpisodes().observe(this@FavoritesFragment, object : Observer<List<FavoriteEpisodeEntity>> {
            override fun onChanged(t: List<FavoriteEpisodeEntity>?) {
                mBinding.recyclerViewFavoriteEpisodes.adapter = adapter
                (mBinding.recyclerViewFavoriteEpisodes.adapter as FavoriteEpisodesAdapter).submitList(t)

            }
        })

        viewModel.progressBarView.set(false)

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}