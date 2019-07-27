package com.furkanaskin.app.podpocket.ui.feed.post_detail

import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import javax.inject.Inject

/**
 * Created by Furkan on 2019-05-26
 */

class PostDetailViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase)