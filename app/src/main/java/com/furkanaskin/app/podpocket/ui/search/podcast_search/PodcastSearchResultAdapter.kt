package com.furkanaskin.app.podpocket.ui.search.podcast_search

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemSearchResultPodcastBinding
import com.furkanaskin.app.podpocket.service.response.ResultsItem

/**
 * Created by Furkan on 30.04.2019
 */

class PodcastSearchResultAdapter(private val callBack: (ResultsItem) -> Unit) : BaseAdapter<ResultsItem>(diffCallbackForPodcast) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemSearchResultPodcastBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_search_result_podcast,
                parent,
                false
        )

        val viewModel = PodcastSearchResultViewModel((parent.context as Activity).application)
        mBinding.viewModel = viewModel

        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack.invoke(it)
            }
        }

        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemSearchResultPodcastBinding).viewModel?.item?.set(getItem(position))
        binding.executePendingBindings()
    }
}

val diffCallbackForPodcast = object : DiffUtil.ItemCallback<ResultsItem>() {
    override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean =
            oldItem == newItem

    override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean =
            oldItem == newItem
}