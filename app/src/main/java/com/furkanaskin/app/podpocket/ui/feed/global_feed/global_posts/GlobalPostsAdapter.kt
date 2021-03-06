package com.furkanaskin.app.podpocket.ui.feed.global_feed.global_posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemGlobalPostBinding
import com.furkanaskin.app.podpocket.db.entities.PostEntity

/**
 * Created by Furkan on 2019-05-31
 */

class GlobalPostsAdapter(private val callBack: (PostEntity, Int) -> Unit) : BaseAdapter<PostEntity>(globalPostsDiffCallback) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemGlobalPostBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_global_post,
            parent,
            false
        )

        val viewModel = GlobalPostsItemViewModel()

        mBinding.viewModel = viewModel

        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack(it, mBinding.viewModel!!.position)
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemGlobalPostBinding).viewModel?.setModel(getItem(position), position)
        binding.executePendingBindings()
    }
}

val globalPostsDiffCallback = object : DiffUtil.ItemCallback<PostEntity>() {
    override fun areContentsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean {
        return oldItem == newItem
    }
}