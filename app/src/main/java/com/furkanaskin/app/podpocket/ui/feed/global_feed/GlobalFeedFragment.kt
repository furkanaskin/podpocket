package com.furkanaskin.app.podpocket.ui.feed.global_feed

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentGlobalFeedBinding
import com.furkanaskin.app.podpocket.db.entities.PostEntity
import com.furkanaskin.app.podpocket.ui.feed.global_feed.global_posts.GlobalPostsAdapter

/**
 * Created by Furkan on 2019-05-26
 */

class GlobalFeedFragment : BaseFragment<GlobalFeedViewModel, FragmentGlobalFeedBinding>(GlobalFeedViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_global_feed

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // val nestedNavController = Navigation.findNavController(view)
        // It's not used but maybe we'll need it later... Don't delete it for now.

        initGlobalFeedAdapter()
        showProgress()

        viewModel.db?.postsDao()?.getPosts()?.observe(this@GlobalFeedFragment, Observer<List<PostEntity>> {
            hideProgress()

            (mBinding.recyclerViewGlobalPosts.adapter as GlobalPostsAdapter).submitList(it)
        })
    }

    private fun initGlobalFeedAdapter() {

        val adapter = GlobalPostsAdapter { item, position ->
            // onClick stuff..
        }

        mBinding.recyclerViewGlobalPosts.adapter = adapter
        mBinding.recyclerViewGlobalPosts.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    }
}