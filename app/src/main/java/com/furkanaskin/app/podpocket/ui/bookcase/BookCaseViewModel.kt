package com.furkanaskin.app.podpocket.ui.bookcase

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel

/**
 * Created by Furkan on 16.04.2019
 */

class BookCaseViewModel(app: Application) : BaseViewModel(app) {
    var progressBarView: ObservableField<Boolean> = ObservableField(false)
    var item: ObservableField<String> = ObservableField("")

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

}