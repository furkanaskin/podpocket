package com.furkanaskin.app.podpocket.ui.profile

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseAdapter
import com.furkanaskin.app.podpocket.databinding.ItemProfileBinding
import com.furkanaskin.app.podpocket.db.entities.ProfileSettingsEntity

/**
 * Created by Furkan on 2019-05-22
 */

class ProfileAdapter(private val callBack: (ProfileSettingsEntity, Int) -> Unit) : BaseAdapter<ProfileSettingsEntity>(profileSettingsDiffCallback) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = DataBindingUtil.inflate<ItemProfileBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_profile,
                parent,
                false)

        val viewModel = ProfileItemViewModel((parent.context as Activity).application)

        mBinding.viewModel = viewModel
        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack.invoke(it, mBinding.viewModel!!.position)
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemProfileBinding).viewModel?.setModel(getItem(position), position)
        binding.executePendingBindings()
    }
}

val profileSettingsDiffCallback = object : DiffUtil.ItemCallback<ProfileSettingsEntity>() {
    override fun areContentsTheSame(oldItem: ProfileSettingsEntity, newItem: ProfileSettingsEntity): Boolean =
            oldItem == newItem

    override fun areItemsTheSame(oldItem: ProfileSettingsEntity, newItem: ProfileSettingsEntity): Boolean =
            oldItem.title == newItem.title


}