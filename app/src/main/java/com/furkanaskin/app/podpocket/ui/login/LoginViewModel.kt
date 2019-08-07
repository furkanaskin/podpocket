package com.furkanaskin.app.podpocket.ui.login

import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import org.jetbrains.anko.doAsync
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

/**
 * Created by Furkan on 14.04.2019
 */

class LoginViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) :
    BaseViewModel(api, appDatabase) {

    var userName: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var agreement: ObservableField<Boolean> = ObservableField(false)

    var forgetPasswordView: ObservableField<Boolean> = ObservableField(true)
    var agreementView: ObservableField<Boolean> = ObservableField(false)
    var buttonText: ObservableInt = ObservableInt(R.string.sign_in)
    var summaryText: ObservableInt = ObservableInt(R.string.already_register)

    var loginSuccess: ObservableField<Boolean> = ObservableField(false)
    var registerSuccess: ObservableField<Boolean> = ObservableField(false)
    var sendMailSuccess: ObservableField<Boolean> = ObservableField(false)
    var verifySuccess: ObservableField<Boolean> = ObservableField()

    var forgetPassIntentLiveData = MutableLiveData<Int>()
    var toastLiveData = MutableLiveData<String>()

    private var type: Int = Constants.LoginActivityType.LOGIN_TYPE

    fun setType(type: Int) {
        this.type = type

        forgetPasswordView.set(type == Constants.LoginActivityType.LOGIN_TYPE)
        agreementView.set(type == Constants.LoginActivityType.REGISTER_TYPE)
        buttonText.set(if (type == Constants.LoginActivityType.LOGIN_TYPE) R.string.sign_in else R.string.register)
        summaryText.set(if (type == Constants.LoginActivityType.LOGIN_TYPE) R.string.already_register else R.string.already_have_account)
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
            toastLiveData.postValue(message)
        }

        return result
    }

    private fun registerClicked() {
        if (getValidationMessages()) {
            progressLiveData.postValue(true)
            mAuth.createUserWithEmailAndPassword(
                userName.get() ?: "",
                password.get()
                    ?: ""
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerSuccess.set(true)
                    verifyEmail()

                    insertUserToFirebase()

                    doAsync {

                        val user = UserEntity(
                            uniqueId = mAuth.currentUser?.uid ?: "",
                            email = userName.get() ?: "",
                            accountCreatedAt = convertDate(LocalDate.now())
                        )

                        db?.userDao()?.insertUser(user)
                    }
                    progressLiveData.postValue(false)
                } else {
                    checkFirebaseCredentials(task)
                }
            }
        }
    }

    private fun loginClicked() {
        if (getValidationMessages()) {
            progressLiveData.postValue(true)

            mAuth.signInWithEmailAndPassword(
                userName.get() ?: "",
                password.get()
                    ?: ""
            ).addOnCompleteListener { task ->

                if (task.isSuccessful && mAuth.currentUser?.isEmailVerified!!) {
                    verifySuccess.set(true)

                    val usersRef =
                        FirebaseDatabase.getInstance().getReference("users").child("${mAuth.currentUser?.uid}")
                    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.getValue(UserEntity::class.java)?.uniqueId != null) {
                                val userFromFirebase = snapshot.getValue(UserEntity::class.java)
                                doAsync {
                                    userFromFirebase?.let { db?.userDao()?.insertUser(it) }

                                    loginSuccess.set(true)
                                    progressLiveData.postValue(false)
                                }
                            } else {
                                loginSuccess.set(true)
                                progressLiveData.postValue(false)
                            }
                        }
                    })
                } else if (task.isSuccessful && mAuth.currentUser?.isEmailVerified == false) {
                    progressLiveData.postValue(false)
                    verifySuccess.set(false)
                } else {
                    checkFirebaseCredentials(task)
                    progressLiveData.postValue(false)
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
        progressLiveData.postValue(true)

        val errorType = task.exception
        var errorMessage: String

        when (errorType) {

            is FirebaseAuthInvalidCredentialsException -> {
                errorMessage = "Lütfen mail adresi ve şifrenizi kontrol ediniz."
                progressLiveData.postValue(false)
            }
            is FirebaseAuthWeakPasswordException -> {
                errorMessage = "Lütfen daha güçlü bir parola deneyiniz."
                progressLiveData.postValue(false)
            }
            is FirebaseAuthUserCollisionException -> {
                errorMessage = "Zaten böyle bir kullanıcı var."
                progressLiveData.postValue(false)
            }
            is FirebaseAuthInvalidUserException -> {
                errorMessage = "Böyle bir kullanıcı bulunamadı."
                progressLiveData.postValue(false)
            }

            else -> {
                Timber.e(errorType.toString())
                errorMessage = "Beklenmedik bir hata oluştu."
                progressLiveData.postValue(false)
            }
        }

        if (!errorMessage.isNullOrEmpty()) {
            toastLiveData.postValue(errorMessage)
        }
    }

    fun forgetPasswordClicked() {

        when (verifySuccess.get()) {
            null -> forgetPassIntentLiveData.postValue(Constants.LoginActivityType.FORGOT_PASS)
            false -> forgetPassIntentLiveData.postValue(Constants.LoginActivityType.EMAIL_VERIFY)
            true -> forgetPassIntentLiveData.postValue(Constants.LoginActivityType.EMAIL_VERIFY)
        }
    }

    private fun insertUserToFirebase() {
        getUser()
        FirebaseDatabase.getInstance().reference.child("users").child(
            mAuth.currentUser?.uid
                ?: ""
        ).setValue(user)
    }

    private fun convertDate(date: LocalDate) =
        date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
}