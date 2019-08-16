package com.furkanaskin.app.podpocket.ui.after_register

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.furkanaskin.app.podpocket.utils.extensions.logE
import com.furkanaskin.app.podpocket.utils.extensions.logV
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

/**
 * Created by Furkan on 21.04.2019
 */

class AfterRegisterViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {

    var name: ObservableField<String> = ObservableField("")
    var userName: ObservableField<String> = ObservableField("")
    var surname: ObservableField<String> = ObservableField("")
    var userBirthDay: ObservableField<String> = ObservableField("")
    var profileImageUrl: ObservableField<String> = ObservableField("")
    var saveSuccess: ObservableField<Boolean> = ObservableField(false)
    var userID: ObservableField<Int> = ObservableField(0)

    fun getValidationMessages(): Boolean {
        var result = true
        var message = ""
        if (name.get().isNullOrEmpty()) {
            result = false
            message = "Lütfen isim alanını boş bırakmayınız."
        } else if (surname.get().isNullOrEmpty()) {
            result = false
            message = "Lütfen soyadı alanını boş bırakmayınız."
        }

        if (message.isNotEmpty()) {
            toastLiveData.postValue(message)
        }

        return result
    }

    fun insertUserToFirebase(updateUser: UserEntity) {
        FirebaseDatabase.getInstance().reference.child("users").child(
            mAuth.currentUser?.uid
                ?: ""
        ).setValue(updateUser).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                logV("user save succes")
            } else {
                logE(task.exception?.printStackTrace().toString())
            }
        }
    }
}