package com.furkanaskin.app.podpocket.ui.bookcase

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.buttonEditText.setOnClickListener {
            Toast.makeText(this.context, "${viewModel.item.get()}", Toast.LENGTH_SHORT).show()
        }
    }


}