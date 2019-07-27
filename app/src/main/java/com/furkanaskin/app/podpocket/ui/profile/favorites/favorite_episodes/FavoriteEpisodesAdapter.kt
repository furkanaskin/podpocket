package com.furkanaskin.app.podpocket.ui.profile.favorites.favorite_episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemFavoriteEpisodesBinding
import com.furkanaskin.app.podpocket.db.entities.FavoriteEpisodeEntity

/**
 * Created by Furkan on 2019-05-18
 */

class FavoriteEpisodesAdapter(private val callBack: (FavoriteEpisodeEntity, Int) -> Unit) : BaseAdapter<FavoriteEpisodeEntity>(favoriteEpisodeDiffCallback) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemFavoriteEpisodesBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_favorite_episodes,
            parent,
            false
        )

        val viewModel = FavoriteEpisodesListViewModel()

        mBinding.viewModel = viewModel
        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack(it, mBinding.viewModel!!.position)
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemFavoriteEpisodesBinding).viewModel?.setModel(getItem(position), position)
        binding.executePendingBindings()
    }
}

val favoriteEpisodeDiffCallback = object : DiffUtil.ItemCallback<FavoriteEpisodeEntity>() {
    override fun areContentsTheSame(oldItem: FavoriteEpisodeEntity, newItem: FavoriteEpisodeEntity): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: FavoriteEpisodeEntity, newItem: FavoriteEpisodeEntity): Boolean =
        oldItem.id == newItem.id
}