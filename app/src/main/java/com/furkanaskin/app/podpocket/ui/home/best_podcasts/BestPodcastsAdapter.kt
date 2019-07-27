package com.furkanaskin.app.podpocket.ui.home.best_podcasts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemBestPodcastsBinding
import com.furkanaskin.app.podpocket.service.response.Podcasts

/**
 * Created by Furkan on 28.04.2019
 */

class BestPodcastsAdapter(private val callBack: (Podcasts) -> Unit) : BaseAdapter<Podcasts>(diffCallback) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemBestPodcastsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_best_podcasts,
            parent,
            false
        )
        val viewModel = BestPodcastsListItemViewModel()
        mBinding.viewModel = viewModel

        mBinding.rootView.setOnClickListener {
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

val diffCallback = object : DiffUtil.ItemCallback<Podcasts>() {
    override fun areContentsTheSame(oldItem: Podcasts, newItem: Podcasts): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: Podcasts, newItem: Podcasts): Boolean =
        oldItem.id == newItem.id
}