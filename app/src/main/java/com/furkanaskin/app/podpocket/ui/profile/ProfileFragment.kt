package com.furkanaskin.app.podpocket.ui.profile

import android.os.Bundle
import android.view.View
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentProfileBinding
import com.furkanaskin.app.podpocket.db.entities.UserEntity

/**
 * Created by Furkan on 16.04.2019
 */

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(ProfileViewModel::class.java) {
    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_profile


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (user == null) {
            //TODO üye girişine yönlendirecek.
            return
        }

        mBinding.viewModel?.userData = user
        editProfile()
    }

    fun editProfile() {
        mBinding.buttonSave.setOnClickListener {
            val user = UserEntity(
                    id = user?.id ?: 0,
                    uniqueId = user?.uniqueId ?: "",
                    name = mBinding.editTextName.text?.toString(),
                    surname = mBinding.editTextSurname.text?.toString(),
                    birthday = mBinding.etBirthday.text?.toString(),
                    email = mBinding.editTextEmail.text?.toString(),
                    mostLovedCategory = mBinding.editTextMostLovedCategory.text?.toString(),
                    lastPlayedPodcast = user?.lastPlayedPodcast,
                    lastPlayedEpisode = user?.lastPlayedEpisode)

            viewModel.changeUserData(user)
        }

    }

}