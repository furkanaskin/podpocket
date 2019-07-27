package com.furkanaskin.app.podpocket.ui.feed.new_post

import android.os.Bundle
import androidx.databinding.Observable
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentNewPostBinding
import com.furkanaskin.app.podpocket.utils.extensions.toast

/**
 * Created by Furkan on 2019-05-26
 */

class NewPostFragment : BaseFragment<NewPostViewModel, FragmentNewPostBinding>(NewPostViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_new_post

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.pushPostSuccess.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                navigate(R.id.action_newPostFragment_to_feedFragment)
                toast("Post başarıyla paylaşıldı.")
            }
        })
    }
}