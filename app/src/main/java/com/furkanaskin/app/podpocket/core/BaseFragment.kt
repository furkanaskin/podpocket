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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.furkanaskin.app.podpocket.utils.extensions.toast
import dagger.android.AndroidInjection
import org.jetbrains.anko.support.v4.runOnUiThread

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) :
    Fragment() {

    lateinit var viewModel: VM
    open lateinit var mBinding: DB
    fun init(inflater: LayoutInflater, container: ViewGroup) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
    }

    open fun init() {}
    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun initViewModel()

    private fun getViewM(): VM =
        ViewModelProviders.of(this, (activity as BaseActivity<*, *>).viewModelProviderFactory).get(mViewModelClass)

    open fun onInject() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(activity)
        super.onCreate(savedInstanceState)
        viewModel = getViewM()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init(inflater, container!!)
        initViewModel()
        init()
        initToast()
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
            if (activity != null && (activity as? BaseActivity<*, *>)?.isShow() == false)
            (activity as? BaseActivity<*, *>)?.dialog?.show()
        }
    }

    fun hideProgress() {
        runOnUiThread {
            if (activity != null)
            (activity as BaseActivity<*, *>).dialog?.dismiss()
        }
    }

    fun initToast() {
        if (viewModel.toastLiveData.hasActiveObservers())
            viewModel.toastLiveData.removeObservers(this)

        viewModel.toastLiveData.observe(
            this@BaseFragment,
            Observer<String> {
                toast(it, Toast.LENGTH_LONG)
            }
        )
    }
}