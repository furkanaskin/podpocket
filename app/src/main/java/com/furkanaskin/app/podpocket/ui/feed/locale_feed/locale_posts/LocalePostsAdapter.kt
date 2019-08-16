package com.furkanaskin.app.podpocket.ui.feed.locale_feed.locale_posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemLocalePostBinding
import com.furkanaskin.app.podpocket.db.entities.PostEntity

/**
 * Created by Furkan on 2019-06-25
 */

class LocalePostsAdapter(private val callBack: (PostEntity, Int) -> Unit) : BaseAdapter<PostEntity>(localePostsDiffCallback) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemLocalePostBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_locale_post,
            parent,
            false
        )

        val viewModel = LocalePostsItemViewModel()

        mBinding.viewModel = viewModel

        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack(it, mBinding.viewModel!!.position)
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as? ItemLocalePostBinding)?.viewModel?.setModel(getItem(position), position)
        binding.executePendingBindings()
    }
}

val localePostsDiffCallback = object : DiffUtil.ItemCallback<PostEntity>() {
    override fun areContentsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean {
        return oldItem == newItem
    }
}