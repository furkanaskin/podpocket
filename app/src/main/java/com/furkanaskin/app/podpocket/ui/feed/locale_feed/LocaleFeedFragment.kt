package com.furkanaskin.app.podpocket.ui.feed.locale_feed

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentLocaleFeedBinding
import com.furkanaskin.app.podpocket.db.entities.PostEntity
import com.furkanaskin.app.podpocket.ui.feed.locale_feed.locale_posts.LocalePostsAdapter

/**
 * Created by Furkan on 2019-05-26
 */

class LocaleFeedFragment : BaseFragment<LocaleFeedViewModel, FragmentLocaleFeedBinding>(LocaleFeedViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_locale_feed

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // val nestedNavController = Navigation.findNavController(view)
        // It's not used but maybe we'll need it later... Don't delete it for now.

        initLocaleFeedAdapter()
        showProgress()

        viewModel.db.postsDao().getLocalePosts(viewModel.currentLocation).observe(this@LocaleFeedFragment, Observer<List<PostEntity>> {
            hideProgress()

            (mBinding.recyclerViewLocalePosts.adapter as LocalePostsAdapter).submitList(it)
        })
    }

    private fun initLocaleFeedAdapter() {

        val adapter = LocalePostsAdapter { item, position ->
            // onClick stuff..
        }

        mBinding.recyclerViewLocalePosts.adapter = adapter
        mBinding.recyclerViewLocalePosts.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    }
}