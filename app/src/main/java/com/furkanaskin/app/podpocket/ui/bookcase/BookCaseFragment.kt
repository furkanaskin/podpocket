package com.furkanaskin.app.podpocket.ui.bookcase

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentBookCaseBinding

/**
 * Created by Furkan on 16.04.2019
 */

class BookCaseFragment : BaseFragment<BookCaseViewModel, FragmentBookCaseBinding>(BookCaseViewModel::class.java) {
    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_book_case
    override fun init() {
    }
}