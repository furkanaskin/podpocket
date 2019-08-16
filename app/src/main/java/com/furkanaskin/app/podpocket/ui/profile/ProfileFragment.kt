package com.furkanaskin.app.podpocket.ui.profile

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentProfileBinding
import com.furkanaskin.app.podpocket.db.entities.ProfileSettingsEntity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.main.MainActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * Created by Furkan on 16.04.2019
 */

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(ProfileViewModel::class.java) {
    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_profile

    override fun init() {
        initProfileItemsAdapter()
        prepareProfileItems()
        initUserDetails()

        mBinding.imageViewProfilePicture.setOnClickListener {
            navigateAccountDetailScreen()
        }
    }

    private fun prepareProfileItems() {
        val itemList = ArrayList<ProfileSettingsEntity>()
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.favorites), R.drawable.ic_favorites))
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.recently_played), R.drawable.ic_recently_plays))
        itemList.add(
            ProfileSettingsEntity(
                resources.getString(R.string.account_details),
                R.drawable.ic_account_settings
            )
        )
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.rate_us), R.drawable.ic_rate_us))
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.give_feedback), R.drawable.ic_give_feedback))
        itemList.add(ProfileSettingsEntity(resources.getString(R.string.logout), R.drawable.ic_exit))

        (mBinding.recyclerViewProfile.adapter as? ProfileAdapter)?.submitList(itemList)
    }

    private fun initProfileItemsAdapter() {
        val adapter = ProfileAdapter { _, position ->

            when (position) {
                0 -> navigateFavoritesScreen()
                1 -> navigateRecentlyPlayedScreen()
                2 -> navigateAccountDetailScreen()
                4 -> sendFeedback()
                5 -> showLogOutAlertDialog()
            }
        }

        mBinding.recyclerViewProfile.adapter = adapter
        mBinding.recyclerViewProfile.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun initUserDetails() {
        if (viewModel.userName.get().isNullOrEmpty()) {
            doAsync {
                viewModel.getUser()
                runOnUiThread {
                    viewModel.userName.set(viewModel.user?.userName ?: "")
                    viewModel.userEmail.set(viewModel.user?.email ?: "")
                    viewModel.userUniqueID.set(viewModel.user?.uniqueId ?: "")
                }
            }
        }
    }

    private fun showLogOutAlertDialog() {
        alert {
            ctx.setTheme(R.style.Theme_MaterialComponents_Light_Dialog)

            message = "Gerçekten çıkış yapmak istiyor musunuz?"
            positiveButton(
                "Evet",
                object : (DialogInterface) -> Unit {
                    override fun invoke(p1: DialogInterface) {
                        logout()
                    }
                }
            )

            negativeButton(
                "Hayır",
                object : (DialogInterface) -> Unit {
                    override fun invoke(p1: DialogInterface) {
                    }
                }
            )
        }.show()
    }

    private fun logout() {
        doAsync {
            viewModel.db?.userDao()?.deleteAll()
            viewModel.db?.postsDao()?.deleteAll()
            viewModel.db?.favoritesDao()?.deleteAllFavoriteEpisodes()
            viewModel.db?.recentlyPlaysDao()?.deleteAll()
            viewModel.db?.episodesDao()?.deleteAllEpisodes()
            viewModel.db?.playerDao()?.deleteAll()

            runOnUiThread {
                viewModel.mAuth.signOut()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:askinn.furkan@gmail.com") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.thanks_for_sharing_expreience))
        if (intent.resolveActivity((activity as? DashboardActivity)?.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun navigateAccountDetailScreen() {
        navigate(R.id.action_profileFragment_to_accountDetailFragment)
    }

    private fun navigateRecentlyPlayedScreen() {
        navigate(R.id.action_profileFragment_to_recentlyPlayedFragment)
    }

    private fun navigateFavoritesScreen() {
        navigate(R.id.action_profileFragment_to_favoritesFragment)
    }
}