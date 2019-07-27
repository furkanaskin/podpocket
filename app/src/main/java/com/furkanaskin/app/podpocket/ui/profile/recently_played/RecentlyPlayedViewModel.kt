package com.furkanaskin.app.podpocket.ui.profile.recently_played

import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import javax.inject.Inject

/**
 * Created by Furkan on 17.05.2019
 */

class RecentlyPlayedViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase)