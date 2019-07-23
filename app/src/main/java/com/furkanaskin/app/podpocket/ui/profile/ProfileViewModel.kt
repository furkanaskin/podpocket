package com.furkanaskin.app.podpocket.ui.profile

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel

/**
 * Created by Furkan on 16.04.2019
 */

class ProfileViewModel : BaseViewModel() {

    var userName: ObservableField<String> = ObservableField("")
    var userEmail: ObservableField<String> = ObservableField("")
    var userUniqueID: ObservableField<String> = ObservableField("")

}