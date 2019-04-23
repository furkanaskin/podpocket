package com.furkanaskin.app.podpocket.ui.after_register

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import javax.inject.Inject

/**
 * Created by Furkan on 21.04.2019
 */

class AfterRegisterViewModel(app: Application) : BaseViewModel(app) {
    var progressBarView: ObservableField<Boolean> = ObservableField(false)

    var userName: ObservableField<String> = ObservableField("")
    var userSurname: ObservableField<String> = ObservableField("")
    var userBirthDay: ObservableField<String> = ObservableField("")
    var saveSuccess: ObservableField<Boolean> = ObservableField(false)
    var userID: ObservableField<Int> = ObservableField(0)

    private lateinit var mAuth: FirebaseAuth

    @Inject
    lateinit var db: AppDatabase


    init {
        (app as? Podpocket)?.component?.inject(this)
    }


    fun saveClicked() {

        if (getValidationMessages() && userID.get() != null) {
            progressBarView.set(true)
            initFirebase()
            getCurrentUserID()

            val uniqueID: String? = mAuth.currentUser?.uid

            doAsync {

                val willBeUpdated = UserEntity(
                        id = userID.get()!!,
                        uniqueId = uniqueID,
                        email = mAuth.currentUser?.email,
                        name = userName.get(),
                        surname = userSurname.get(),
                        birthday = userBirthDay.get())


                db.userDao().updateUser(willBeUpdated)

            }

            progressBarView.set(false)

        }

    }

    private fun getCurrentUserID() {

        val uniqueID = mAuth.currentUser?.uid
        val currentUser = uniqueID.let { db.userDao().getUser(it!!) }

        currentUser.observeForever {
            userID.set(currentUser.value?.id)

            if (userID.get() != null) {
                getCurrentUserID()
            }
        }
    }

    private fun getValidationMessages(): Boolean {
        var result = true
        var message = ""
        if (userName.get().isNullOrEmpty()) {
            result = false
            message = "Lütfen isim alanını boş bırakmayınız."
        } else if (userSurname.get().isNullOrEmpty()) {
            result = false
            message = "Lütfen soyadı alanını boş bırakmayınız."
        }

        if (message.isNotEmpty()) {
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
        }

        return result
    }


    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()

    }

}