package com.furkanaskin.app.podpocket.ui.login

import android.app.Application
import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.ui.forget_password.ForgetPasswordActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.doAsync
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
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
    var verifySuccess: ObservableField<Boolean> = ObservableField()

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
            showProgress()
            mAuth.createUserWithEmailAndPassword(userName.get() ?: "", password.get()
                    ?: "").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerSuccess.set(true)
                    verifyEmail()

                    insertUserToFirebase()

                    doAsync {

                        val user = UserEntity(
                                uniqueId = mAuth.currentUser?.uid ?: "",
                                email = userName.get() ?: "",
                                accountCreatedAt = convertDate(LocalDate.now()).reversed())

                        db.userDao().insertUser(user)
                    }

                    hideProgress()
                } else {
                    checkFirebaseCredentials(task)
                }
            }
        }
    }


    private fun loginClicked() {
        if (getValidationMessages()) {
            showProgress()
            mAuth.signInWithEmailAndPassword(userName.get() ?: "", password.get()
                    ?: "").addOnCompleteListener { task ->

                if (task.isSuccessful && mAuth.currentUser?.isEmailVerified!!) {

                    verifySuccess.set(true)

                    insertUserToFirebase() // Save user to firebase.

                    doAsync {
                        val user = UserEntity(
                                uniqueId = mAuth.currentUser?.uid ?: "",
                                email = userName.get() ?: "")

                        db.userDao().insertUser(user)

                    }

                    loginSuccess.set(true)
                    hideProgress()
                } else {
                    verifySuccess.set(false)
                    checkFirebaseCredentials(task)
                    hideProgress()
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
        showProgress()
        val errorType = task.exception
        var errorMessage: String

        when (errorType) {

            is FirebaseAuthInvalidCredentialsException -> {
                errorMessage = "Lütfen mail adresi ve şifrenizi kontrol ediniz."
                hideProgress()
            }
            is FirebaseAuthWeakPasswordException -> {
                errorMessage = "Lütfen daha güçlü bir parola deneyiniz."
                hideProgress()
            }
            is FirebaseAuthUserCollisionException -> {
                errorMessage = "Zaten böyle bir kullanıcı var."
                hideProgress()
            }
            is FirebaseAuthInvalidUserException -> {
                errorMessage = "Böyle bir kullanıcı bulunamadı."
                hideProgress()
            }

            else -> {
                Timber.e(errorType.toString())
                errorMessage = "Beklenmedik bir hata oluştu."
                hideProgress()
            }
        }

        if (!errorMessage.isNullOrEmpty()) {
            Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_LONG).show()
        }

    }


    fun forgetPasswordClicked() {

        when (verifySuccess.get()) {
            null -> {
                val intent = Intent(getApplication(), ForgetPasswordActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("TYPE", Constants.LoginActivityType.FORGOT_PASS)
                getApplication<Application>().startActivity(intent)
            }

            false -> {
                val intent = Intent(getApplication(), ForgetPasswordActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("TYPE", Constants.LoginActivityType.EMAIL_VERIFY)
                getApplication<Application>().startActivity(intent)
            }

            true -> {
                val intent = Intent(getApplication(), ForgetPasswordActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("TYPE", Constants.LoginActivityType.EMAIL_VERIFY)
                getApplication<Application>().startActivity(intent)
            }
        }

    }

    private fun insertUserToFirebase() {
        getUser()
        FirebaseDatabase.getInstance().reference.child("users").child(mAuth.currentUser?.uid
                ?: "").setValue(user)
    }

    private fun convertDate(date: LocalDate) =
            date.format(DateTimeFormatter.ISO_DATE)

    override fun onCleared() {
        super.onCleared()

        disposables.clear()
    }
}