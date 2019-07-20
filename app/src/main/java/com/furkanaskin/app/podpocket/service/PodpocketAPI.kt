package com.furkanaskin.app.podpocket.service

import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.service.response.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Furkan on 18.04.2019
 */

interface PodpocketAPI {

    // Get Requests

    @GET("search")
    fun fullTextSearch(@Query("q") searchTerm: String, @Query("type") type: String, @Query("offset") offset: Int): Observable<Search>

    @GET("search")
    fun fullTextSearchWithGenres(@Query("q") searchTerm: String, @Query("type") type: String, @Query("genre_ids") genreIds: String, @Query("offset") offset: Int): Observable<Search>

    @GET("typeahead")
    fun typeAheadSearch(@Query("q") searchTerm: String, @Query("show_podcasts") showPodcasts: Int, @Query("show_genres") showGenres: Int, @Query("safe_mode") safeMode: Int): Observable<TypeAhead>

    @GET("podcasts/{id}")
    fun getPodcastById(@Path("id") podcastId: String): Observable<Podcasts>

    @GET("podcasts/{id}")
    fun getPodcastByIdWithPaging(@Path("id") podcastId: String, @Query("next_episode_pub_date") nextEpisodePubDate: Long): Observable<Podcasts>

    @GET("episodes/{id}")
    fun getEpisodeById(@Path("id") episodeId: String): Observable<Episode>

    @GET("curated_podcasts/{id}")
    fun getCuratedPodcastsById(@Path("id") curatedPodcastId: String): Observable<CuratedPodcasts>

    @GET("best_podcasts")
    fun getBestPodcasts(@Query("region") region: String, @Query("safe_mode") explicitContent: Int): Observable<Resource<BestPodcasts>>

    @GET("genres")
    fun getGenres(): Observable<Genres>

    @GET("regions")
    fun getRegions(): Observable<Regions>

    @GET("languages")
    fun getLanguages(): Observable<Languages>

    @GET("just_listen")
    fun getRandomPodcast(): Observable<JustListen>

    @GET("curated_podcasts")
    fun getCuratedPodcasts(): Observable<CuratedPodcasts>

    @GET("podcasts/{id}/recommendations")
    fun getPodcastRecommendations(@Path("id") podcastId: String, @Query("safe_mod") explicitContent: Int): Observable<PodcastRecommendations>

    @GET("episodes/{id}/recommendations")
    fun getEpisodeRecommendations(@Path("id") episodeId: String, @Query("safe_mod") explicitContent: Int): Observable<EpisodeRecommendations>

    // Post Requests

    @POST("episodes")
    fun postEpisodes(@Query("ids") ids: List<String>): Observable<EpisodesItem>

    @POST("podcasts")
    fun postPodcasts(@Query("ids") ids: List<String>): Observable<PodcastsItem>

    @POST("podcasts/submit")
    fun registerPodcast(@Query("rss") rssUrl: String, @Query("email") email: String): Observable<PodcastsItem>

}