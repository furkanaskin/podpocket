package com.furkanaskin.app.podpocket.ui.profile

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import javax.inject.Inject

/**
 * Created by Furkan on 16.04.2019
 */

class ProfileViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {

    var userName: ObservableField<String> = ObservableField("")
    var userEmail: ObservableField<String> = ObservableField("")
    var userUniqueID: ObservableField<String> = ObservableField("")
}