package com.furkanaskin.app.podpocket.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentProfileBinding
import com.furkanaskin.app.podpocket.db.entities.ProfileSettingsEntity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.main.MainActivity
import com.google.firebase.storage.FirebaseStorage

/**
 * Created by Furkan on 16.04.2019
 */

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(ProfileViewModel::class.java) {
    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_profile

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    var profileImageUrl: ObservableField<String> = ObservableField("")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserDetails()
        initProfileItemsAdapter()
        prepareProfileItems()
        getProfilePicture()

        mBinding.imageViewProfilePicture.setOnClickListener {
            navigateAccountDetailScreen()
        }

    }

    fun prepareProfileItems() {
        var itemList = ArrayList<ProfileSettingsEntity>()
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.favorites), R.drawable.ic_favorites))
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.recently_played), R.drawable.ic_recently_plays))
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.account_details), R.drawable.ic_account_settings))
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.rate_us), R.drawable.ic_rate_us))
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.give_feedback), R.drawable.ic_give_feedback))
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.logout), R.drawable.ic_exit))

        (mBinding.recyclerViewProfile.adapter as ProfileAdapter).submitList(itemList)
    }


    private fun initProfileItemsAdapter() {
        val adapter = ProfileAdapter { item, position ->

            when (position) {
                0 -> navigateFavoritesScreen()
                1 -> navigateRecentlyPlayedScreen()
                2 -> navigateAccountDetailScreen()
                4 -> sendFeedback()
                5 -> logout()
            }
        }

        mBinding.recyclerViewProfile.adapter = adapter
        mBinding.recyclerViewProfile.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    }

    private fun initUserDetails() {
        viewModel.userName.set(user?.userName ?: "")
        viewModel.userEmail.set(user?.email ?: "")
    }

    fun logout() {
        mAuth.signOut()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun getProfilePicture() {
        val profilePicturePath = mAuth.currentUser?.uid + "_" + "profile_picture.jpg"
        storageRef.child(profilePicturePath).downloadUrl.addOnSuccessListener {
            profileImageUrl.set(it.toString())
            Glide.with(this.activity!!).load(user!!.profilePictureUrl).into(mBinding.imageViewProfilePicture)
            hideProgress()
        }
    }

    fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:askinn.furkan@gmail.com") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Thanks for sharing your experience with Podpocket Developers!")
        if (intent.resolveActivity((activity as DashboardActivity).packageManager) != null) {
            startActivity(intent)
        }
    }

    fun navigateAccountDetailScreen() {
        navigate(R.id.action_profileFragment_to_accountDetailFragment)
    }

    fun navigateRecentlyPlayedScreen() {
        navigate(R.id.action_profileFragment_to_recentlyPlayedFragment)
    }

    fun navigateFavoritesScreen() {
        navigate(R.id.action_profileFragment_to_favoritesFragment)
    }
}