package com.furkanaskin.app.podpocket.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) : Fragment() {
    lateinit var viewModel: VM
    open lateinit var mBinding: DB
    fun init(inflater: LayoutInflater, container: ViewGroup) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
    }

    lateinit var mAuth: FirebaseAuth
    var user: UserEntity? = null

    open fun init() {}
    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun initViewModel()

    private fun getViewM(): VM = ViewModelProviders.of(this).get(mViewModelClass)
    open fun onInject() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewM()

        initFirebase()
        getUser()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        init(inflater, container!!)
        initViewModel()
        super.onCreateView(inflater, container, savedInstanceState)
        return mBinding.root
    }

    open fun refresh() {}

    open fun navigate(action: Int) {
        view?.let { _view ->
            Navigation.findNavController(_view).navigate(action)
        }
    }

    fun showProgress() {
        runOnUiThread {
            if (activity != null)
                if ((activity as BaseActivity<*, *>).isShow() == false)
                    (activity as BaseActivity<*, *>).dialog?.show()
        }
    }


    fun hideProgress() {
        runOnUiThread {
            if (activity != null)
                (activity as BaseActivity<*, *>).dialog?.dismiss()
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