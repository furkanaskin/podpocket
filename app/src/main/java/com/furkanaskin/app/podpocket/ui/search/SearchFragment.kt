package com.furkanaskin.app.podpocket.ui.search

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.databinding.FragmentSearchBinding
import com.furkanaskin.app.podpocket.service.response.Genres
import com.furkanaskin.app.podpocket.service.response.ResultsItem
import com.furkanaskin.app.podpocket.service.response.Search
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.ui.search.episode_search.SearchResultAdapter
import com.furkanaskin.app.podpocket.ui.search.podcast_search.PodcastSearchResultAdapter
import com.furkanaskin.app.podpocket.utils.PaginationScrollListener
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by Furkan on 16.04.2019
 */

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(SearchViewModel::class.java) {
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var episodesOffset: Int = 0
    var podcastsOffset: Int = 0
    var totalPodcastResult: Int = 0
    var totalEpisodeResult: Int = 0
    var searchTerm: String? = ""
    var episodesResult: MutableList<ResultsItem?>? = mutableListOf()
    var podcastsResult: MutableList<ResultsItem?>? = mutableListOf()

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun init() {

        initSearchView()
        initSearchAdapter()
        initGenres()
        initVisibilities()
        showResults()


        if (viewModel.progressLiveData.hasActiveObservers())
            viewModel.progressLiveData.removeObservers(this)

        viewModel.progressLiveData.observe(this@SearchFragment, Observer<Boolean> {
            if (it)
                showProgress()
            else
                hideProgress()
        })

    }

    private fun initSearchAdapter() {

        // -- EPISODE --
        val searchEpisodeAdapter = SearchResultAdapter { item ->

            viewModel.getEpisodes(item.podcastId ?: "")

            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putStringArrayListExtra(Constants.IntentName.PLAYER_ACTIVITY_ALL_IDS, viewModel.podcastEpisodeIds.value)
            intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION, item.id)
            startActivity(intent)
        }

        val episodesLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.recyclerViewEpisodeSearchResult.adapter = searchEpisodeAdapter
        mBinding.recyclerViewEpisodeSearchResult.layoutManager = episodesLayoutManager
        mBinding.recyclerViewEpisodeSearchResult.addOnScrollListener(object : PaginationScrollListener(episodesLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                if (totalEpisodeResult != layoutManager.itemCount) {
                    getSearchResult(searchTerm ?: "", Constants.SearchQuery.EPISODE, episodesOffset)
                }
            }
        })


        // -- PODCAST --
        val searchPodcastAdapter = PodcastSearchResultAdapter { item ->
            val podcastId = item.id
            val action = SearchFragmentDirections.actionSearchFragmentToPodcastFragment()
            action.podcastID = podcastId ?: ""
            findNavController().navigate(action)
        }

        val podcastsLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mBinding.recyclerViewPodcastSearchResult.adapter = searchPodcastAdapter
        mBinding.recyclerViewPodcastSearchResult.layoutManager = podcastsLayoutManager
        mBinding.recyclerViewPodcastSearchResult.addOnScrollListener(object : PaginationScrollListener(podcastsLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                if (totalPodcastResult != layoutManager.itemCount) {
                    getSearchResult(searchTerm ?: "", Constants.SearchQuery.PODCAST, podcastsOffset)
                }
            }

        })
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
        searchViewCloseIcon.setImageResource(R.color.mainBackgroundColor)
        val linearLayoutSearchView: ViewGroup = searchViewSearchIcon.parent as ViewGroup
        linearLayoutSearchView.removeView(searchViewSearchIcon)
        linearLayoutSearchView.addView(searchViewSearchIcon)
        var timer = Timer()

        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    searchTerm = newText
                    episodesResult?.clear()
                    podcastsResult?.clear()
                    getSearchResult(newText, Constants.SearchQuery.EPISODE, episodesOffset)
                    getSearchResult(newText, Constants.SearchQuery.PODCAST, podcastsOffset)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewCloseIcon.hide()
                if (newText?.length!! % 3 == 0 && newText.isNotEmpty()) {
                    timer.cancel()
                    searchTerm = newText

                    val sleep = 1500L
                    timer = Timer()
                    timer.schedule(sleep) {
                        episodesResult?.clear()
                        podcastsResult?.clear()
                        episodesOffset = 0
                        podcastsOffset = 0
                        getSearchResult(newText.toString(), Constants.SearchQuery.EPISODE, episodesOffset)
                        getSearchResult(newText.toString(), Constants.SearchQuery.PODCAST, podcastsOffset)
                    }
                }

                if (newText.isEmpty()) {
                    episodesResult?.clear()
                    podcastsResult?.clear()
                }
                return true
            }
        })
    }

    private fun showResults() {

        if (viewModel.episodeSearchResultLiveData.hasActiveObservers())
            viewModel.episodeSearchResultLiveData.removeObservers(this)

        viewModel.episodeSearchResultLiveData.observe(this@SearchFragment, Observer<Resource<Search>> {
            isLoading = false
            episodesOffset = it.data?.nextOffset ?: 0
            totalEpisodeResult = it.data?.total ?: 0

            it.data?.results?.forEach {
                episodesResult?.add(it)
            }

            (mBinding.recyclerViewEpisodeSearchResult.adapter as SearchResultAdapter).submitList(episodesResult)
        })


        if (viewModel.podcastSearchResultLiveData.hasActiveObservers())
            viewModel.podcastSearchResultLiveData.removeObservers(this)

        viewModel.podcastSearchResultLiveData.observe(this@SearchFragment, Observer<Resource<Search>> {
            isLoading = false
            podcastsOffset = it.data?.nextOffset ?: 0
            totalPodcastResult = it.data?.total ?: 0

            it.data?.results?.forEach {
                podcastsResult?.add(it)
            }
            (mBinding.recyclerViewPodcastSearchResult.adapter as PodcastSearchResultAdapter).submitList(podcastsResult)
        })
    }

    private fun getSearchResult(searchText: String, type: String, offset: Int) {

        if (viewModel.selectedGenres.size == 0) {
            viewModel.getSearchResult(searchText, type, offset)

        } else {
            val genresIds = viewModel.selectedGenres.joinToString(separator = ",")
            viewModel.getSearchResultWithGenres(searchText, type, genresIds, offset)
        }
    }

    private fun initGenres() {
        viewModel.getGenres()

        if (viewModel.genresLiveData.hasActiveObservers())
            viewModel.genresLiveData.removeObservers(this)

        viewModel.genresLiveData.observe(this@SearchFragment, Observer<Resource<Genres>> {
            it.data?.let { genres -> addChipToGroup(mBinding.chipGroupGenres, genres) }
        })
    }

    private fun addChipToGroup(chipGroup: ChipGroup, items: Genres) {

        items.genres?.forEachIndexed { _, genre ->

            val chip = Chip(context)
            chip.text = genre?.name
            chip.tag = genre?.id

            chip.isClickable = true
            chip.isCheckable = true
            chip.isCloseIconVisible = false
            chip.isCheckedIconVisible = false
            chip.setChipBackgroundColorResource(R.color.mainBackgroundColor)
            chip.chipStrokeWidth = 1f
            chip.setChipStrokeColorResource(R.color.colorLoginText)
            chip.setTextColor(ContextCompat.getColor(this.context!!, R.color.white))

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewModel.selectedGenres.add(buttonView.tag as Int)
                    buttonView.setTextColor(ContextCompat.getColor(this.context!!, R.color.colorCyan))
                    chip.setChipStrokeColorResource(R.color.colorCyan)
                } else {
                    viewModel.selectedGenres.remove(buttonView.tag as Int)
                    buttonView.setTextColor(ContextCompat.getColor(this.context!!, R.color.white))
                    chip.setChipStrokeColorResource(R.color.colorLoginText)
                }
            }

            chipGroup.addView(chip)
        }
    }


    fun initVisibilities() {
        if (viewModel.episodeHeadingLiveData.hasActiveObservers())
            viewModel.episodeHeadingLiveData.removeObservers(this)

        viewModel.episodeHeadingLiveData.observe(this@SearchFragment, Observer<Boolean> {
            if (it) {
                mBinding.textViewSearchEpisodesHeading.visibility = View.VISIBLE
                mBinding.recyclerViewEpisodeSearchResult.visibility = View.VISIBLE
            } else {
                mBinding.textViewSearchEpisodesHeading.visibility = View.GONE
                mBinding.recyclerViewEpisodeSearchResult.visibility = View.GONE

            }
        })

        if (viewModel.podcastHeadingLiveData.hasActiveObservers())
            viewModel.podcastHeadingLiveData.removeObservers(this)

        viewModel.podcastHeadingLiveData.observe(this@SearchFragment, Observer<Boolean> {
            if (it) {
                mBinding.textViewSearchPodcastsHeading.visibility = View.VISIBLE
                mBinding.recyclerViewPodcastSearchResult.visibility = View.VISIBLE
            } else {
                mBinding.textViewSearchPodcastsHeading.visibility = View.GONE
                mBinding.recyclerViewPodcastSearchResult.visibility = View.GONE

            }
        })
    }
}