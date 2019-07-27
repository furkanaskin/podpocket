package com.furkanaskin.app.podpocket.ui.profile.recently_played.podcasts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemRecentlyPlaysPodcastBinding
import com.furkanaskin.app.podpocket.db.entities.RecentlyPlaysEntity

/**
 * Created by Furkan on 2019-07-03
 */

class RecentlyPodcastsAdapter(private val clickCallback: ((RecentlyPlaysEntity) -> Unit)?) : BaseAdapter<RecentlyPlaysEntity>(object : DiffUtil.ItemCallback<RecentlyPlaysEntity>() {
    override fun areItemsTheSame(oldItem: RecentlyPlaysEntity, newItem: RecentlyPlaysEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecentlyPlaysEntity, newItem: RecentlyPlaysEntity): Boolean {
        return oldItem == newItem
    }
}) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val viewModel = RecentlyPodcastsItemViewModel()
        val binding: ItemRecentlyPlaysPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_recently_plays_podcast, parent, false)
        binding.viewModel = viewModel

        binding.cardView.setOnClickListener {
            binding.viewModel?.item?.get()?.let {
                clickCallback?.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemRecentlyPlaysPodcastBinding).viewModel?.item?.set(getItem(position))
        binding.executePendingBindings()
    }
}
