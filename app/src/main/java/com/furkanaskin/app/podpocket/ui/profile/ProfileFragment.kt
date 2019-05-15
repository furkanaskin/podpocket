package com.furkanaskin.app.podpocket.ui.profile

import android.os.Bundle
import android.view.View
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentProfileBinding
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import org.jetbrains.anko.doAsync

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
                    uniqueId = mAuth.currentUser?.uid ?: "",
                    name = mBinding.editTextNameSurname.text?.toString(),
                    surname = mBinding.editTextNameSurname.text?.toString(),
                    birthday = mBinding.etBirthday.text?.toString(),
                    email = mBinding.editTextEmail.text?.toString())

            viewModel.changeUserData(user)
        }

    }

}