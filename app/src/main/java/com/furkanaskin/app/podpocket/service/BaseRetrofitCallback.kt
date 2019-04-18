package com.furkanaskin.app.podpocket.service

import com.furkanaskin.app.podpocket.core.BaseCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Furkan on 18.04.2019
 */

abstract class BaseRetrofitCallback<T>(private val callBack: BaseCallBack<T>?) : Callback<T> {

    private val API_ERROR = "Api Error"


    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        callBack?.let {

            if (response?.isSuccessful != true) {
                callBack.onFail(API_ERROR)
                Timber.e(response?.message())
                return
            }

            val apiResponse = response.body()
            if (apiResponse == null) {
                callBack.onFail(API_ERROR)
                return
            }

            callBack.onSuccess(response.body()!!)

        }
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        callBack?.let {
            t?.let { callBack.onFail(t.message ?: API_ERROR) }
            Timber.e(t)
        }
    }
}