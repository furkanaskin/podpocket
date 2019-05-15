package com.furkanaskin.app.podpocket.ui.player

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.FragmentPlayerQueueBinding
import com.furkanaskin.app.podpocket.db.entities.PlayerEntity
import com.furkanaskin.app.podpocket.service.response.EpisodesItem
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.player.queue.QueueAdapter
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Furkan on 6.05.2019
 */

class PlayerQueueFragment : BaseFragment<PlayerQueueViewModel, FragmentPlayerQueueBinding>(PlayerQueueViewModel::class.java) {

    private val podcastId by lazy { arguments?.getString("podcastId") as String }
    val disposable = CompositeDisposable()
    val queue = mutableListOf<EpisodesItem?>()
    var player: PlayerEntity? = null
    var prevPos: Int = -1
    var deleteSelected: Int = 0

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.progressBar.visibility = View.VISIBLE

        viewModel.db.playerDao().getPlayer().observe(this, Observer<PlayerEntity> { t -> player = t })

        val adapter = QueueAdapter { item, position, _ ->

            // "Talk is cheap, show me the code."
            // Linus Torvalds..

            if (deleteSelected == 0) {
                queue[(activity as PlayerActivity).currentPosition]?.isSelected = false
                deleteSelected += 1
            }

            if (prevPos == -1) {
                item.isSelected = true
                prevPos = position
                (activity as PlayerActivity).getEpisodeDetail(item.id ?: "")
                (activity as PlayerActivity).currentPosition = position
                updateData()
            } else {
                queue[prevPos]?.isSelected = false
                item.isSelected = true
                prevPos = position
                (activity as PlayerActivity).getEpisodeDetail(item.id ?: "")
                (activity as PlayerActivity).currentPosition = position
                updateData()
            }

        }

        disposable.add(viewModel.getEpisodes(podcastId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: Podcasts) {
                        t.episodes?.forEach {
                            queue.add(it)
                        }

                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)

                        Timber.e(e)
                        mBinding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Beklenmedik bir hata oluştu.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onComplete() {
                        super.onComplete()
                        queue[(activity as PlayerActivity).currentPosition]?.isSelected = true
                        (mBinding.recyclerViewQueueEpisodes.adapter as QueueAdapter).submitList(queue)
                        mBinding.progressBar.visibility = View.GONE
                    }

                }))

        mBinding.recyclerViewQueueEpisodes.adapter = adapter

    }

    fun updateData() {
        mBinding.recyclerViewQueueEpisodes.adapter?.notifyDataSetChanged()
    }
}