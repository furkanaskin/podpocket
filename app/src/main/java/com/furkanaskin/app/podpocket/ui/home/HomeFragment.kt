package com.furkanaskin.app.podpocket.ui.home

import android.os.Bundle
import android.view.View
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentHomeBinding
import com.furkanaskin.app.podpocket.service.response.BestPodcasts
import com.furkanaskin.app.podpocket.ui.home.best_podcasts.BestPodcastsAdapter
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

        initAdapter()
        initBestPodcasts()

    }

    fun initAdapter() {
        val adapter = BestPodcastsAdapter { item ->

        }

        mBinding.recyclerViewBestPodcasts.adapter = adapter
        // mBinding.recyclerViewBestPodcasts.addItemDecoration(PodPocketItemDecoration(30))
    }

    private fun initBestPodcasts() {

        disposable.add(viewModel.api.getBestPodcasts("tr", 1)
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }


}