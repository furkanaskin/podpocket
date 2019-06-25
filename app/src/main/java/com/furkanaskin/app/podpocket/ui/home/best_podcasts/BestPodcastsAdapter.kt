package com.furkanaskin.app.podpocket.ui.home.best_podcasts

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemBestPodcastsBinding
import com.furkanaskin.app.podpocket.service.response.ChannelsItem

/**
 * Created by Furkan on 28.04.2019
 */

class BestPodcastsAdapter(private val callBack: (ChannelsItem) -> Unit) : BaseAdapter<ChannelsItem>(diffCallback) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemBestPodcastsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_best_podcasts,
                parent,
                false
        )
        val viewModel = BestPodcastsListItemViewModel((parent.context as Activity).application)
        mBinding.viewModel = viewModel

        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack.invoke(it)
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemBestPodcastsBinding).viewModel?.item?.set(getItem(position))
        binding.executePendingBindings()
    }
}

val diffCallback = object : DiffUtil.ItemCallback<ChannelsItem>() {
    override fun areContentsTheSame(oldItem: ChannelsItem, newItem: ChannelsItem): Boolean =
            oldItem == newItem

    override fun areItemsTheSame(oldItem: ChannelsItem, newItem: ChannelsItem): Boolean =
            oldItem.id == newItem.id
}