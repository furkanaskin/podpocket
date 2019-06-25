package com.furkanaskin.app.podpocket.ui.feed.locale_feed.locale_posts

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemLocalePostBinding
import com.furkanaskin.app.podpocket.model.Post

/**
 * Created by Furkan on 2019-06-25
 */

class LocalePostsAdapter(private val callBack: (Post, Int) -> Unit) : BaseAdapter<Post>(localePostsDiffCallback) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemLocalePostBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_locale_post,
                parent,
                false)

        val viewModel = LocalePostsItemViewModel((parent.context as Activity).application)

        mBinding.viewModel = viewModel

        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack(it, mBinding.viewModel!!.position)
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemLocalePostBinding).viewModel?.setModel(getItem(position), position)
        binding.executePendingBindings()
    }
}

val localePostsDiffCallback = object : DiffUtil.ItemCallback<Post>() {
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}