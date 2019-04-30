package com.furkanaskin.app.podpocket.ui.after_register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.Observable
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityAfterRegisterBinding
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 21.04.2019
 */

class AfterRegisterActivity : BaseActivity<AfterRegisterViewModel, ActivityAfterRegisterBinding>(AfterRegisterViewModel::class.java) {
    override fun getLayoutRes() = R.layout.activity_after_register

    override fun initViewModel(viewModel: AfterRegisterViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (isCurrentUserIDAvailable()) {
            true -> binding.buttonDone.visibility = View.VISIBLE
            false -> binding.buttonDone.visibility = View.GONE
        }

        binding.buttonDone.setOnClickListener {
            if (viewModel.getValidationMessages() && viewModel.userID.get() != null) {
                viewModel.progressBarView.set(true)

                doAsync {

                    val willBeUpdated = UserEntity(
                            id = user.id,
                            uniqueId = user.uniqueId,
                            email = user.email,
                            name = viewModel.userName.get(),
                            surname = viewModel.userSurname.get(),
                            birthday = viewModel.userBirthDay.get())


                    viewModel.db.userDao().updateUser(willBeUpdated)
                    viewModel.saveSuccess.set(true)

                }

                viewModel.progressBarView.set(false)

            }
        }


        viewModel.saveSuccess.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                Thread.sleep(100)
                val intent = Intent(this@AfterRegisterActivity, DashboardActivity::class.java)
                runOnUiThread {
                    startActivity(intent)
                    finish()
                }

            }
        })

        getUser()


    }


    private fun isCurrentUserIDAvailable(): Boolean {
        return viewModel.userID.get() != null
    }

    private fun getUser() {
        doAsync {
            user = viewModel.mAuth.currentUser?.uid?.let { viewModel.db.userDao().getUser(it) }!!
        }
    }

}