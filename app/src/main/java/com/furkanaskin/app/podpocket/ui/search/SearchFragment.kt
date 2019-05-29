package com.furkanaskin.app.podpocket.ui.search

import android.content.Intent
import android.os.Handler
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
import com.furkanaskin.app.podpocket.databinding.FragmentSearchBinding
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity
import com.furkanaskin.app.podpocket.service.response.Genres
import com.furkanaskin.app.podpocket.service.response.GenresItem
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.service.response.Search
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
import com.furkanaskin.app.podpocket.ui.search.episode_search.SearchResultAdapter
import com.furkanaskin.app.podpocket.ui.search.podcast_search.PodcastSearchResultAdapter
import com.furkanaskin.app.podpocket.utils.service.CallbackWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 16.04.2019
 */

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(SearchViewModel::class.java) {
    private val disposable = CompositeDisposable()
    private val genreList = mutableListOf<GenresItem?>()
    var ids: ArrayList<String> = ArrayList()

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_search


    override fun init() {
        // getGenres() - To be added later.
        initSearchView()
        initSearchAdapter()
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
                                    val episodesItem = episode.let { EpisodeEntity(it!!) }
                                    episodesItem.let { viewModel.db.episodesDao().insertEpisode(it) }
                                }
                            }
                        }

                        override fun onComplete() {
                            super.onComplete()

                            val intent = Intent(activity, PlayerActivity::class.java)
                            intent.putStringArrayListExtra("allPodIds", ids)
                            intent.putExtra("position", item.id)
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
        searchViewCloseIcon.setImageResource(R.color.mainBackgroundColor)
        val linearLayoutSearchView: ViewGroup = searchViewSearchIcon.parent as ViewGroup
        linearLayoutSearchView.removeView(searchViewSearchIcon)
        linearLayoutSearchView.addView(searchViewSearchIcon)

        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewCloseIcon.visibility = View.GONE
                if (newText?.length!! % 3 == 0)
                    Handler().postDelayed({
                        getSearchResult(newText.toString(), "episode")
                        getSearchResult(newText.toString(), "podcast")

                    }, 1000)
                return true
            }

        })
    }

    private fun getSearchResult(searchText: String, type: String) {

        showProgress()
        disposable.add(viewModel.getSearchResult(searchText, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<Search>(viewModel.getApplication()) {
                    override fun onSuccess(t: Search) {
                        hideProgress()
                        if (type.equals("episode")) {
                            (mBinding.recyclerViewEpisodeSearchResult.adapter as SearchResultAdapter).submitList(t.results)
                        } else {
                            (mBinding.recyclerViewPodcastSearchResult.adapter as PodcastSearchResultAdapter).submitList(t.results)

                        }

                    }

                }))
    }

}