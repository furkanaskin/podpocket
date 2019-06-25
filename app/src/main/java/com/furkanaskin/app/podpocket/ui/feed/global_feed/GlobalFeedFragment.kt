package com.furkanaskin.app.podpocket.ui.feed.global_feed

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentGlobalFeedBinding
import com.furkanaskin.app.podpocket.model.Post
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

        viewModel.posts.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Post>>() {
            override fun onItemRangeRemoved(sender: ObservableList<Post>?, positionStart: Int, itemCount: Int) {
            }

            override fun onItemRangeMoved(sender: ObservableList<Post>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            }

            override fun onItemRangeInserted(sender: ObservableList<Post>?, positionStart: Int, itemCount: Int) {
                hideProgress()
                (mBinding.recyclerViewGlobalPosts.adapter as GlobalPostsAdapter).submitList(viewModel.posts)
                (mBinding.recyclerViewGlobalPosts.adapter as GlobalPostsAdapter).notifyDataSetChanged()
            }

            override fun onItemRangeChanged(sender: ObservableList<Post>?, positionStart: Int, itemCount: Int) {
            }

            override fun onChanged(sender: ObservableList<Post>?) {
                hideProgress()
                (mBinding.recyclerViewGlobalPosts.adapter as GlobalPostsAdapter).submitList(viewModel.posts)
                (mBinding.recyclerViewGlobalPosts.adapter as GlobalPostsAdapter).notifyDataSetChanged()
            }
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