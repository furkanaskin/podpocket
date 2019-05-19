package com.furkanaskin.app.podpocket.ui.search

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentSearchBinding
import com.furkanaskin.app.podpocket.service.response.Genres
import com.furkanaskin.app.podpocket.service.response.GenresItem
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Furkan on 16.04.2019
 */

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(SearchViewModel::class.java) {
    private val disposable = CompositeDisposable()
    private val genreList = mutableListOf<GenresItem?>()

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_search


    override fun init() {
        getGenres()
        initSearchView()

    }


    private fun getGenres() {
        disposable.add(viewModel.getGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Genres>(activity?.application) {
                    override fun onSuccess(t: Genres) {
                        t.genres?.forEach {
                            genreList.add(it)
                        }


                    }

                }))


    }


    private fun initSearchView() {
        val searchEditText: EditText = mBinding.searchView.findViewById(R.id.search_src_text)
        activity?.applicationContext?.let { ContextCompat.getColor(it, R.color.white) }?.let { searchEditText.setTextColor(it) }
        activity?.applicationContext?.let { ContextCompat.getColor(it, R.color.grayTextColor) }?.let { searchEditText.setHintTextColor(it) }
        mBinding.searchView.isActivated = true
        mBinding.searchView.setIconifiedByDefault(false)
        mBinding.searchView.isIconified = false
        val searchViewSearchIcon = mBinding.searchView.findViewById<ImageView>(R.id.search_mag_icon)
        val searchViewCloseIcon = mBinding.searchView.findViewById<ImageView>(R.id.search_close_btn)
        searchViewSearchIcon.setImageResource(R.drawable.ic_search)
        val linearLayoutSearchView: ViewGroup = searchViewSearchIcon.parent as ViewGroup
        linearLayoutSearchView.removeView(searchViewSearchIcon)
        linearLayoutSearchView.addView(searchViewSearchIcon)

        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewCloseIcon.visibility = View.GONE
                return true
            }

        })
    }


}