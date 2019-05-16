package com.furkanaskin.app.podpocket.utils.service

import android.app.Application
import android.widget.Toast
import com.furkanaskin.app.podpocket.service.response.Error
import com.google.gson.Gson
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.jetbrains.anko.runOnUiThread
import retrofit2.HttpException
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

abstract class CallbackWrapper<T : Any>(application: Application?) : DisposableObserver<T>() {
    var weakReference: WeakReference<Application?>? = WeakReference(application)
    protected abstract fun onSuccess(t: T)
    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        val message: String?
        if (e is HttpException) {
            message = getErrorMessage(e.response().errorBody())
        } else if (e is SocketTimeoutException) {
            message = "Sunucudan cevap alınamadı."
        } else if (e is IOException) {
            message = "Ağ ile ilgili bir hata oluştu. Lütfen tekrar deneyiniz."
        } else {
            message = e.message
        }

        if (message.isNullOrEmpty().not())
            weakReference?.get()?.let { app ->
                app.runOnUiThread {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onComplete() {
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String? {
        return try {
            val error = Gson().fromJson<Error>(responseBody?.string(), Error::class.java)

            "${error.errors[0].data.detail!!.substringAfterLast("/")} ${error.errors[0].data.pointer}"
        } catch (e: Exception) {
            ""
        }

    }

}