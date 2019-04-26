package com.furkanaskin.app.podpocket.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import timber.log.Timber

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) : Fragment() {
    lateinit var viewModel: VM
    open lateinit var mBinding: DB
    fun init(inflater: LayoutInflater, container: ViewGroup) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
    }

    lateinit var mAuth: FirebaseAuth
    lateinit var user: UserEntity

    open fun init() {}
    @LayoutRes
    abstract fun getLayoutRes(): Int

    private fun getViewM(): VM = ViewModelProviders.of(this).get(mViewModelClass)
    open fun onInject() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewM()

        try {
            if (!::user.isInitialized || !::mAuth.isInitialized) {  // if initialized, don't initialize again.
                initFirebase()
                getUser()
            }
        } catch (e: UninitializedPropertyAccessException) { // For unexpected crash.
            Timber.e(e)
            Toast.makeText(this.context, "Beklenmeyen bir hata oluştu, lütfen tekrar deneyiniz.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        init(inflater, container!!)
        init()
        super.onCreateView(inflater, container, savedInstanceState)
        return mBinding.root
    }

    open fun refresh() {}

    open fun navigate(action: Int) {
        view?.let { _view ->
            Navigation.findNavController(_view).navigate(action)
        }
    }


    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()

    }

    private fun getUser() {
        doAsync {
            user = viewModel.mAuth.currentUser?.uid?.let { viewModel.db.userDao().getUser(it) }!!
        }
    }
}