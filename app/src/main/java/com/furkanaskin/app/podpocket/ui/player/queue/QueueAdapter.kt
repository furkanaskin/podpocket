package com.furkanaskin.app.podpocket.ui.player.queue

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemQueueBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity

/**
 * Created by Furkan on 14.05.2019
 */

class QueueAdapter(private val callBack: (EpisodeEntity, Int, Boolean) -> Unit) : BaseAdapter<EpisodeEntity>(queueDiffCallback) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemQueueBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_queue,
                parent,
                false)

        val viewModel = QueueListItemViewModel((parent.context as Activity).application)

        mBinding.viewModel = viewModel

        mBinding.relativeLayoutItemContainer.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack.invoke(it, mBinding.viewModel!!.position, mBinding.viewModel!!.isSelected)
            }
        }

        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemQueueBinding).viewModel?.setModel(getItem(position), position, getItem(position).isSelected
                ?: false)

        binding.executePendingBindings()

    }

}

val queueDiffCallback = object : DiffUtil.ItemCallback<EpisodeEntity>() {
    override fun areContentsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean =
            oldItem == newItem

    override fun areItemsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean =
            oldItem.id == newItem.id
}