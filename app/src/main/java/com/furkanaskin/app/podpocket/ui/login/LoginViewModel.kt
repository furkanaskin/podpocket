package com.furkanaskin.app.podpocket.ui.login

import android.app.Application
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.service.response.Search
import com.furkanaskin.app.podpocket.ui.forget_password.ForgetPasswordActivity
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
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
    var progressBarView: ObservableField<Boolean> = ObservableField(false)

    var loginSuccess: ObservableField<Boolean> = ObservableField(false)
    var registerSuccess: ObservableField<Boolean> = ObservableField(false)
    var sendMailSuccess: ObservableField<Boolean> = ObservableField(false)

    private val disposables = CompositeDisposable()

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

    fun alreadyHasAccount() {
        setType(if (type == Constants.LoginActivityType.LOGIN_TYPE) Constants.LoginActivityType.REGISTER_TYPE else Constants.LoginActivityType.LOGIN_TYPE)
    }

    fun buttonClick() {
        if (type == Constants.LoginActivityType.LOGIN_TYPE) {
            loginClicked()
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
            progressBarView.set(true)
            mAuth.createUserWithEmailAndPassword(userName.get() ?: "", password.get()
                    ?: "").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerSuccess.set(true)
                    verifyEmail()


                    doAsync {

                        val user = UserEntity(
                                uniqueId = mAuth.currentUser?.uid ?: "",
                                email = userName.get() ?: "",
                                password = password.get() ?: "")

                        db.userDao().insertUser(user)
                    }


                    progressBarView.set(false)
                } else {
                    checkFirebaseCredentials(task)
                }
            }
        }
    }

    private fun loginClicked() {
        if (getValidationMessages()) {
            progressBarView.set(true)
            mAuth.signInWithEmailAndPassword(userName.get()!!, password.get()!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginSuccess.set(true)
                    progressBarView.set(false)
                } else {
                    checkFirebaseCredentials(task)
                    progressBarView.set(false)
                }
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


    private fun checkFirebaseCredentials(task: Task<AuthResult>) {
        progressBarView.set(true)
        val errorType = task.exception
        var errorMessage: String

        when (errorType) {

            is FirebaseAuthInvalidCredentialsException -> {
                errorMessage = "Lütfen mail adresi ve şifrenizi kontrol ediniz."
                progressBarView.set(false)
            }
            is FirebaseAuthWeakPasswordException -> {
                errorMessage = "Lütfen daha güçlü bir parola deneyiniz."
                progressBarView.set(false)
            }
            is FirebaseAuthUserCollisionException -> {
                errorMessage = "Zaten böyle bir kullanıcı var."
                progressBarView.set(false)
            }
            is FirebaseAuthInvalidUserException -> {
                errorMessage = "Böyle bir kullanıcı bulunamadı."
                progressBarView.set(false)
            }
            else -> {
                Timber.v(errorType.toString())
                errorMessage = "Beklenmedik bir hata oluştu.."
                progressBarView.set(false)
            }
        }

        if (!errorMessage.isNullOrEmpty()) {
            Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_LONG).show()
        }

    }

    fun forgetPasswordClicked() {

        val intent = Intent(getApplication(), ForgetPasswordActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(intent)

    }

    fun testAPI() {
        disposables.add(api.fullTextSearch("Junior Talks", 0, "", 1, "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Search>(getApplication()) {
                    override fun onSuccess(t: Search) {
                        Log.v("Request", "Success")
                    }

                    override fun onError(e: Throwable) {
                    }

                }))
    }

    override fun onCleared() {
        super.onCleared()

        disposables.clear()
    }
}