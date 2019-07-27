package com.furkanaskin.app.podpocket.ui.search.episode_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemSearchResultBinding
import com.furkanaskin.app.podpocket.service.response.ResultsItem

/**
 * Created by Furkan on 30.04.2019
 */

class SearchResultAdapter(private val callBack: (ResultsItem) -> Unit) : BaseAdapter<ResultsItem>(diffCallback) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemSearchResultBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search_result,
            parent,
            false
        )

        val viewModel = EpisodeSearchResultViewModel()
        mBinding.viewModel = viewModel

        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack(it)
            }
        }

        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemSearchResultBinding).viewModel?.item?.set(getItem(position))
        binding.executePendingBindings()
    }
}

val diffCallback = object : DiffUtil.ItemCallback<ResultsItem>() {
    override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean =
        oldItem == newItem
}