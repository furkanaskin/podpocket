package com.furkanaskin.app.podpocket.ui.podcast

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentPodcastBinding
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 29.04.2019
 */

class PodcastFragment : BaseFragment<PodcastViewModel, FragmentPodcastBinding>(PodcastViewModel::class.java) {

    private val podcastFragmentArgs: PodcastFragmentArgs by navArgs()
    private val disposable = CompositeDisposable()

    override fun getLayoutRes(): Int = R.layout.fragment_podcast

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Delete previous episodes.
        doAsync {
            viewModel.db.episodesDao().deleteAllEpisodes()
        }

        getData()
    }

    fun getData() {
        showProgress()
        disposable.add(viewModel.getEpisodes(podcastFragmentArgs.podcastID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {
                    override fun onSuccess(t: Podcasts) {
                        viewModel.podcast.set(t)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        hideProgress()
                        initAdapter()
                    }
                }))
    }

    private fun initAdapter() {
        childFragmentManager.let { fragmentManager ->

            viewModel.podcast.get()?.let { podcasts ->
                PodcastFragmentPagerAdapter(context, fragmentManager, podcasts)
                        .also { mBinding.viewPager.adapter = it }
            }
        }

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}