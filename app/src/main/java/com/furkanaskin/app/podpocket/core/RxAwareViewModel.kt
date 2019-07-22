package com.furkanaskin.app.podpocket.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Furkan on 2019-07-22
 */

/**
 * reference : https://github.com/googlesamples/android-architecture-components
 */
open class RxAwareViewModel(app: Application) : AndroidViewModel(app) {
    val disposable = CompositeDisposable()

    override fun onCleared() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onCleared()
    }
}