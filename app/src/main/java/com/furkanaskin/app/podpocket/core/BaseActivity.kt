package com.furkanaskin.app.podpocket.core

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.utils.PodPocketProgressDialog
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    lateinit var mAuth: FirebaseAuth
    var user: UserEntity? = null
    var dialog: Dialog? = null

    val binding by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes()) as DB
    }

    val viewModel by lazy {
        ViewModelProviders.of(this).get(mViewModelClass)
    }

    /**
     * If you want to inject Dependency Injection
     * on your activity, you can override this.
     */
    open fun onInject() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(viewModel)
        initDialog()
        onInject()

        initFirebase()
        getUser()
    }

    /**
     *
     *  You need override this method.
     *  And you need to set viewModel to binding: binding.viewModel = viewModel
     *
     */
    abstract fun initViewModel(viewModel: VM)

    fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()

    }

    private fun initDialog() {
        dialog = PodPocketProgressDialog.progressDialog(this)
    }

    fun isShow(): Boolean? {
        return dialog?.isShowing
    }

    fun showProgress() {
        runOnUiThread {
            dialog?.show()
        }
    }

    fun hideProgress() {
        runOnUiThread {
            dialog?.dismiss()
        }
    }
    fun getUser() {
        doAsync {
            user = viewModel.mAuth.currentUser?.uid?.let { viewModel.db.userDao().getUser(it) }!!
        }
    }
}