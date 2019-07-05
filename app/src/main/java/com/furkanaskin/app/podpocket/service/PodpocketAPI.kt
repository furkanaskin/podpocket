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
    fun fullTextSearch(@Query("q") searchTerm: String, @Query("type") type: String): Observable<Search>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("search")
    fun fullTextSearchWithGenres(@Query("q") searchTerm: String, @Query("type") type: String, @Query("genre_ids") genreIds: String): Observable<Search>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("typeahead")
    fun typeAheadSearch(@Query("q") searchTerm: String, @Query("show_podcasts") showPodcasts: Int, @Query("show_genres") showGenres: Int, @Query("safe_mode") safeMode: Int): Observable<TypeAhead>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("podcasts/{id}")
    fun getPodcastById(@Path("id") podcastId: String): Observable<Podcasts>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("podcasts/{id}")
    fun getPodcastByIdWithPaging(@Path("id") podcastId: String, @Query("next_episode_pub_date") nextEpisodePubDate: Long): Observable<Podcasts>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("episodes/{id}")
    fun getEpisodeById(@Path("id") episodeId: String): Observable<Episode>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("curated_podcasts/{id}")
    fun getCuratedPodcastsById(@Path("id") curatedPodcastId: String): Observable<CuratedPodcasts>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("best_podcasts")
    fun getBestPodcasts(@Query("region") region: String, @Query("safe_mode") explicitContent: Int): Observable<BestPodcasts>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("genres")
    fun getGenres(): Observable<Genres>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("regions")
    fun getRegions(): Observable<Regions>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("languages")
    fun getLanguages(): Observable<Languages>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("just_listen")
    fun getRandomPodcast(): Observable<JustListen>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("curated_podcasts")
    fun getCuratedPodcasts(): Observable<CuratedPodcasts>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("podcasts/{id}/recommendations")
    fun getPodcastRecommendations(@Path("id") podcastId: String, @Query("safe_mod") explicitContent: Int): Observable<PodcastRecommendations>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @GET("episodes/{id}/recommendations")
    fun getEpisodeRecommendations(@Path("id") episodeId: String, @Query("safe_mod") explicitContent: Int): Observable<EpisodeRecommendations>

    // Post Requests

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @POST("episodes")
    fun postEpisodes(@Query("ids") ids: List<String>): Observable<EpisodesItem>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @POST("podcasts")
    fun postPodcasts(@Query("ids") ids: List<String>): Observable<PodcastsItem>

    @Headers("X-ListenAPI-Key: 82e6628b74404fb9a26a934b7d1adfa0")
    @POST("podcasts/submit")
    fun registerPodcast(@Query("rss") rssUrl: String, @Query("email") email: String): Observable<PodcastsItem>

}