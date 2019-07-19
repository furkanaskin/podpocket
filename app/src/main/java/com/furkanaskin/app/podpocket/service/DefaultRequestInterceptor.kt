package com.furkanaskin.app.podpocket.service

import com.furkanaskin.app.podpocket.core.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Furkan on 2019-07-19
 */

@Singleton
class DefaultRequestInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(with(chain.request().newBuilder()) {
            addHeader(Constants.NetworkService.API_KEY_HEADER_NAME, Constants.NetworkService.API_KEY_VALUE)
            build()
        })
    }
}