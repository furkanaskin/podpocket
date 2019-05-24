package com.furkanaskin.app.podpocket.ui.after_register

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel

/**
 * Created by Furkan on 21.04.2019
 */

class AfterRegisterViewModel(app: Application) : BaseViewModel(app) {

    var progressBarView: ObservableField<Boolean> = ObservableField(false)
    var name: ObservableField<String> = ObservableField("")
    var userName: ObservableField<String> = ObservableField("")
    var surname: ObservableField<String> = ObservableField("")
    var userBirthDay: ObservableField<String> = ObservableField("")
    var profileImageUrl: ObservableField<String> = ObservableField("")
    var saveSuccess: ObservableField<Boolean> = ObservableField(false)
    var userID: ObservableField<Int> = ObservableField(0)

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

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
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
        }

        return result
    }

}