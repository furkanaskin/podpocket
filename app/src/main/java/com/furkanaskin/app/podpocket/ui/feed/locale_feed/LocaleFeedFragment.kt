package com.furkanaskin.app.podpocket.ui.feed.locale_feed

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentLocaleFeedBinding

/**
 * Created by Furkan on 2019-05-26
 */

class LocaleFeedFragment : BaseFragment<LocaleFeedViewModel, FragmentLocaleFeedBinding>(LocaleFeedViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_locale_feed

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedNavController = Navigation.findNavController(view)

        mBinding.button.setOnClickListener {
            nestedNavController.navigate(R.id.action_localeFeedFragment_to_newPostFragment)
        }
    }

}