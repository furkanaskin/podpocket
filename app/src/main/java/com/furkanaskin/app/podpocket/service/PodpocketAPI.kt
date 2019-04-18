package com.furkanaskin.app.podpocket.service

import com.furkanaskin.app.podpocket.service.response.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by Furkan on 18.04.2019
 */

interface PodpocketAPI {

    // Get Requests

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("search")
    fun fullTextSearch(@Query("q") searchTerm: String): Observable<Search>

    @GET("typeahead")
    fun typeAheadSearch(@Query("q") searchTerm: String): Observable<TypeAhead>

    @GET("podcasts/{id}")
    fun getPodcastsById(@Path("id") podcastId: String): Observable<Podcasts>

    @GET("episodes/{id}")
    fun getEpisodesById(@Path("id") episodeId: String): Observable<Episodes>

    @GET("curated_podcasts/{id}")
    fun getCuratedPodcastsById(@Path("id") curatedPodcastId: String): Observable<CuratedPodcasts>

    @GET("best_podcasts")
    fun getBestPodcasts(@Query("region") region: String, @Query("safe_mode") explicitContent: Int): Observable<BestPodcasts>

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
    fun getEpisodesRecommendations(@Path("id") episodeId: String, @Query("safe_mod") explicitContent: Int): Observable<EpisodeRecommendations>

    // Post Requests

    @POST("episodes")
    fun postEpisodes(@Query("ids") ids: List<String>): Observable<EpisodesItem>

    @POST("podcasts")
    fun postPodcasts(@Query("ids") ids: List<String>): Observable<PodcastsItem>

    @POST("podcasts/submit")
    fun registerPodcast(@Query("rss") rssUrl: String, @Query("email") email: String): Observable<PodcastsItem>

}