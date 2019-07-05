package com.furkanaskin.app.podpocket.ui.search

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.FragmentSearchBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.response.Genres
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.service.response.Search
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.ui.search.episode_search.SearchResultAdapter
import com.furkanaskin.app.podpocket.ui.search.podcast_search.PodcastSearchResultAdapter
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 16.04.2019
 */

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(SearchViewModel::class.java) {
    private val disposable = CompositeDisposable()
    var ids: ArrayList<String> = ArrayList()

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_search


    override fun init() {

        initSearchView()
        initSearchAdapter()
        initGenres()

    }

    private fun initSearchAdapter() {

        // -- EPISODE --
        val searchEpisodeAdapter = SearchResultAdapter { item ->

            disposable.add(viewModel.getEpisodes(item.podcastId ?: "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : CallbackWrapper<Podcasts>(viewModel.getApplication()) {

                        override fun onSuccess(t: Podcasts) {
                            t.episodes?.forEachIndexed { _, episodesItem ->
                                ids.add(episodesItem?.id ?: "")
                            }

                            doAsync {
                                viewModel.db.episodesDao().deleteAllEpisodes()
                                t.episodes?.forEachIndexed { _, episode ->
                                    val episodesItem = episode?.let { EpisodeEntity(it) }
                                    episodesItem.let { viewModel.db.episodesDao().insertEpisode(it) }
                                }
                            }
                        }

                        override fun onComplete() {
                            super.onComplete()

                            val intent = Intent(activity, PlayerActivity::class.java)
                            intent.putStringArrayListExtra(Constants.IntentName.PLAYER_ACTIVITY_ALL_IDS, ids)
                            intent.putExtra(Constants.IntentName.PLAYER_ACTIVITY_POSITION, item.id)
                            startActivity(intent)

                        }
                    }))
        }

        mBinding.recyclerViewEpisodeSearchResult.adapter = searchEpisodeAdapter
        mBinding.recyclerViewEpisodeSearchResult.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)


        // -- PODCAST --
        val searchPodcastAdapter = PodcastSearchResultAdapter { item ->

            val podcastId = item.id
            val action = SearchFragmentDirections.actionSearchFragmentToPodcastEpisodesFragment()
            action.podcastID = podcastId ?: ""
            findNavController().navigate(action)
        }

        mBinding.recyclerViewPodcastSearchResult.adapter = searchPodcastAdapter
        mBinding.recyclerViewPodcastSearchResult.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)

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

        // Hide headings
        setEpisodesHeadingVisibility(false)
        setPodcastsHeadingVisibility(false)

        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    getSearchResult(newText, Constants.SearchQuery.EPISODE)
                    getSearchResult(newText, Constants.SearchQuery.PODCAST)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewCloseIcon.hide()
                if (newText?.length!! % 3 == 0 && newText.isNotEmpty()) {
                    getSearchResult(newText.toString(), Constants.SearchQuery.EPISODE)
                    getSearchResult(newText.toString(), Constants.SearchQuery.PODCAST)
                }

                if (newText.isEmpty()) {
                    setEpisodesHeadingVisibility(false)
                    setPodcastsHeadingVisibility(false)
                }
                return true
            }

        })
    }

    private fun getSearchResult(searchText: String, type: String) {


        if (viewModel.selectedGenres.size == 0) {
            disposable.add(viewModel.getSearchResult(searchText, type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : CallbackWrapper<Search>(viewModel.getApplication()) {
                        override fun onSuccess(t: Search) {
                            if (type == Constants.SearchQuery.EPISODE && t.results != null) {
                                setEpisodesHeadingVisibility(true)
                                setPodcastsHeadingVisibility(true)
                                (mBinding.recyclerViewEpisodeSearchResult.adapter as SearchResultAdapter).submitList(t.results)
                            } else if (type == Constants.SearchQuery.PODCAST && t.results != null) {
                                setEpisodesHeadingVisibility(true)
                                setPodcastsHeadingVisibility(true)
                                (mBinding.recyclerViewPodcastSearchResult.adapter as PodcastSearchResultAdapter).submitList(t.results)
                            } else {
                                setEpisodesHeadingVisibility(false)
                                setPodcastsHeadingVisibility(false)
                            }

                        }

                    }))
        } else {

            val genresIds = viewModel.selectedGenres.joinToString(separator = ",")

            disposable.add(viewModel.getSearchResultWithGenres(searchText, type, genresIds)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : CallbackWrapper<Search>(viewModel.getApplication()) {
                        override fun onSuccess(t: Search) {
                            if (type == Constants.SearchQuery.EPISODE && t.results != null) {
                                setEpisodesHeadingVisibility(true)
                                setPodcastsHeadingVisibility(true)
                                (mBinding.recyclerViewEpisodeSearchResult.adapter as SearchResultAdapter).submitList(t.results)
                            } else if (type == Constants.SearchQuery.PODCAST && t.results != null) {
                                setEpisodesHeadingVisibility(true)
                                setPodcastsHeadingVisibility(true)
                                (mBinding.recyclerViewPodcastSearchResult.adapter as PodcastSearchResultAdapter).submitList(t.results)
                            } else {
                                setEpisodesHeadingVisibility(false)
                                setPodcastsHeadingVisibility(false)
                            }

                        }

                    }))
        }
    }

    private fun initGenres() {
        showProgress()
        disposable.add(viewModel.getGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Genres>(viewModel.getApplication()) {
                    override fun onSuccess(t: Genres) {
                        hideProgress()
                        val genres = t
                        addChipToGroup(mBinding.chipGroupGenres, genres)
                    }

                }))
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

    fun setEpisodesHeadingVisibility(isVisible: Boolean) {
        if (isVisible) {
            mBinding.textViewSearchEpisodesHeading.visibility = View.VISIBLE
            mBinding.recyclerViewEpisodeSearchResult.visibility = View.VISIBLE
        } else {
            mBinding.textViewSearchEpisodesHeading.visibility = View.GONE
            mBinding.recyclerViewEpisodeSearchResult.visibility = View.GONE
        }
    }

    fun setPodcastsHeadingVisibility(isVisible: Boolean) {
        if (isVisible) {
            mBinding.textViewSearchPodcastsHeading.visibility = View.VISIBLE
            mBinding.recyclerViewPodcastSearchResult.visibility = View.VISIBLE
        } else {
            mBinding.textViewSearchPodcastsHeading.visibility = View.GONE
            mBinding.recyclerViewPodcastSearchResult.visibility = View.GONE
        }
    }

}