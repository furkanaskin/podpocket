package com.furkanaskin.app.podpocket.ui.profile.account_detail

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 17.05.2019
 */

class AccountDetailViewModel(app: Application) : BaseViewModel(app) {
    var progressBarView: ObservableField<Boolean> = ObservableField(false)
    var item: ObservableField<String> = ObservableField("")

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    var userData: UserEntity? = null

    fun getName(): String {

        return userData?.name ?: ""
    }


    fun getSurname(): String {

        return userData?.surname ?: ""
    }

    fun changeUserData(user: UserEntity) {

        doAsync {
            db.userDao().updateUser(user)
        }
    }

}