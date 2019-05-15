package com.furkanaskin.app.podpocket.ui.podcast_episodes.episodes

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemPodcastEpisodesBinding
import com.furkanaskin.app.podpocket.service.response.EpisodesItem

/**
 * Created by Furkan on 29.04.2019
 */

class EpisodesAdapter(private val callBack: (EpisodesItem, Int) -> Unit) : BaseAdapter<EpisodesItem>(episodeDiffCallback) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemPodcastEpisodesBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_podcast_episodes,
                parent,
                false)

        val viewModel = EpisodesListItemViewModel((parent.context as Activity).application)

        mBinding.viewModel = viewModel

        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack.invoke(it, mBinding.viewModel!!.position)
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemPodcastEpisodesBinding).viewModel?.setModel(getItem(position), position)
        binding.executePendingBindings()
    }
}

val episodeDiffCallback = object : DiffUtil.ItemCallback<EpisodesItem>() {
    override fun areContentsTheSame(oldItem: EpisodesItem, newItem: EpisodesItem): Boolean =
            oldItem == newItem

    override fun areItemsTheSame(oldItem: EpisodesItem, newItem: EpisodesItem): Boolean =
            oldItem.id == newItem.id


}