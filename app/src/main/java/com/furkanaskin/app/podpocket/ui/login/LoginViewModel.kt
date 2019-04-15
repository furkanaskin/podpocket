package com.furkanaskin.app.podpocket.ui.login

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

/**
 * Created by Furkan on 14.04.2019
 */

class LoginViewModel(app: Application) : BaseViewModel(app) {

    var userName: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var agreement: ObservableField<Boolean> = ObservableField(false)

    var forgetPasswordView: ObservableField<Boolean> = ObservableField(true)
    var agreementView: ObservableField<Boolean> = ObservableField(false)
    var buttonText: ObservableField<String> = ObservableField(getApplication<Application>().getString(R.string.sign_in))
    var summaryText: ObservableField<String> = ObservableField(getApplication<Application>().getString(R.string.already_register))

    var loginSuccess: ObservableField<Boolean> = ObservableField(false)
    var registerSuccess: ObservableField<Boolean> = ObservableField(false)
    var sendMailSuccess: ObservableField<Boolean> = ObservableField(false)

    private lateinit var mAuth: FirebaseAuth

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    private var type: Int = Constants.LoginActivityType.LOGIN_TYPE

    fun setType(type: Int) {
        this.type = type

        forgetPasswordView.set(type == Constants.LoginActivityType.LOGIN_TYPE)
        agreementView.set(type == Constants.LoginActivityType.REGISTER_TYPE)
        buttonText.set(if (type == Constants.LoginActivityType.LOGIN_TYPE) getApplication<Application>().getString(R.string.sign_in) else getApplication<Application>().getString(R.string.register))
        summaryText.set(if (type == Constants.LoginActivityType.LOGIN_TYPE) getApplication<Application>().getString(R.string.already_register) else getApplication<Application>().getString(R.string.already_have_account))
    }

    private fun getType() {

    }

    fun alreadyHasAccount() {
        setType(if (type == Constants.LoginActivityType.LOGIN_TYPE) Constants.LoginActivityType.REGISTER_TYPE else Constants.LoginActivityType.LOGIN_TYPE)
    }

    fun buttonClick() {
        if (type == Constants.LoginActivityType.LOGIN_TYPE) {

        } else if (type == Constants.LoginActivityType.REGISTER_TYPE) {
            registerClicked()
        }
    }

    private fun getValidationMessages(): Boolean {
        var result = true
        var message = ""
        if (userName.get().isNullOrEmpty() || Patterns.EMAIL_ADDRESS.matcher(userName.get()).matches().not()) {
            result = false
            message = "Lütfen girilen e-posta adresinizi kontrol ediniz."
        } else if (password.get().isNullOrEmpty()) {
            result = false
            message = "Lütfen girilen şifreyi kontrol ediniz."
        } else if (password.get()?.length ?: 0 < 6) {
            result = false
            message = "Girilen şifre en az 6 haneli olmalıdır."
        } else if (type == Constants.LoginActivityType.REGISTER_TYPE && agreement.get() != true) {
            result = false
            message = "Lütfen Sözleşmeyi kabul ediniz."
        }

        if (message.isNotEmpty()) {
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
        }

        return result
    }

    private fun registerClicked() {
        if (getValidationMessages()) {
            initFirebase()
            mAuth.createUserWithEmailAndPassword(userName.get()!!, password.get()!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerSuccess.set(true)
                    verifyEmail()
                }
            }.addOnFailureListener { task ->
                Timber.e(task.cause)
            }

        }
    }

    private fun verifyEmail() {
        val mUser = mAuth.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendMailSuccess.set(true)
            }
        }
    }

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()
    }
}