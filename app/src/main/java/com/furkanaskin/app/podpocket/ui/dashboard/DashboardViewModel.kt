package com.furkanaskin.app.podpocket.ui.dashboard

import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import javax.inject.Inject

/**
 * Created by Furkan on 16.04.2019
 */

class DashboardViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase)