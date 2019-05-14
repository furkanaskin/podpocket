package com.furkanaskin.app.podpocket.ui.player.queue

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.EpisodesItem

/**
 * Created by Furkan on 14.05.2019
 */

class QueueListItemViewModel(app: Application) : BaseViewModel(app) {
    var item: ObservableField<EpisodesItem> = ObservableField()
    var position = -1

    fun setModel(item: EpisodesItem, position: Int) {
        this.item.set(item)
        this.position = position

    }
}