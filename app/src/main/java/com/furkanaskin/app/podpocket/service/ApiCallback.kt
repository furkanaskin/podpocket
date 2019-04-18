package com.furkanaskin.app.podpocket.service

import com.furkanaskin.app.podpocket.core.BaseCallBack

/**
 * Created by Furkan on 18.04.2019
 */

class ApiCallback<T>(baseCallBack: BaseCallBack<T>?) : BaseRetrofitCallback<T>(baseCallBack)