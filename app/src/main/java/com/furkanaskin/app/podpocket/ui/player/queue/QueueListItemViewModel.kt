package com.furkanaskin.app.podpocket.ui.player.queue

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity

/**
 * Created by Furkan on 14.05.2019
 */

class QueueListItemViewModel(app: Application) : BaseViewModel(app) {
    var item: ObservableField<EpisodeEntity> = ObservableField()
    var position = -1
    var isSelected: Boolean = false

    fun setModel(item: EpisodeEntity, position: Int, isSelected: Boolean) {
        this.item.set(item)
        this.position = position
        item.isSelected = isSelected
    }
}