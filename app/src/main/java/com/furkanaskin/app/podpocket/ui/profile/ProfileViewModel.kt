package com.furkanaskin.app.podpocket.ui.profile

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 16.04.2019
 */

class ProfileViewModel(app: Application) : BaseViewModel(app) {

    var progressBarView: ObservableField<Boolean> = ObservableField(false)

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    var userData: UserEntity? = null

    fun getNameAndSurname(): String {

        return userData?.name + " " + userData?.surname
    }

    fun changeUserData(user: UserEntity) {

        doAsync {
            db.userDao().insertUser(user)
        }
    }

}