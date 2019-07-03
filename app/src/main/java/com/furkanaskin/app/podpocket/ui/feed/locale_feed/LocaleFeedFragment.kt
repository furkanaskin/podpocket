package com.furkanaskin.app.podpocket.ui.feed.locale_feed

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentLocaleFeedBinding
import com.furkanaskin.app.podpocket.model.Post
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

        viewModel.posts.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Post>>() {
            override fun onItemRangeRemoved(sender: ObservableList<Post>?, positionStart: Int, itemCount: Int) {
            }

            override fun onItemRangeMoved(sender: ObservableList<Post>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            }

            override fun onItemRangeInserted(sender: ObservableList<Post>?, positionStart: Int, itemCount: Int) {
                if (isVisible) {
                    hideProgress()
                    (mBinding.recyclerViewLocalePosts.adapter as LocalePostsAdapter).submitList(viewModel.posts)
                    (mBinding.recyclerViewLocalePosts.adapter as LocalePostsAdapter).notifyDataSetChanged()
                }
            }

            override fun onItemRangeChanged(sender: ObservableList<Post>?, positionStart: Int, itemCount: Int) {
            }

            override fun onChanged(sender: ObservableList<Post>?) {
                if (isVisible) {
                    hideProgress()
                    (mBinding.recyclerViewLocalePosts.adapter as LocalePostsAdapter).submitList(viewModel.posts)
                    (mBinding.recyclerViewLocalePosts.adapter as LocalePostsAdapter).notifyDataSetChanged()
                }
            }
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