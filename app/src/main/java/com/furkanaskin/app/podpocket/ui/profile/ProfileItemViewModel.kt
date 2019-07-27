package com.furkanaskin.app.podpocket.ui.profile

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.entities.ProfileSettingsEntity

/**
 * Created by Furkan on 2019-05-22
 */

class ProfileItemViewModel : BaseViewModel() {
    var item = ObservableField<ProfileSettingsEntity>()
    var position = -1

    fun setModel(item: ProfileSettingsEntity, position: Int) {
        this.item.set(item)
        this.position = position
    }
}