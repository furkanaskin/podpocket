package com.furkanaskin.app.podpocket.core

/**
 * Created by Furkan on 18.04.2019
 */

interface BaseCallBack<T> {
    fun onSuccess(data: T)
    fun onFail(message: String)
}