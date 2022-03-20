package com.devices.touchscreen.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.SPUtils
import com.devices.touchscreen.common.showToast
import com.devices.touchscreen.net.ApiException
import kotlinx.coroutines.*
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


typealias Block<T> = suspend (CoroutineScope) -> T
typealias Error = suspend (Exception) -> Unit
typealias Cancel = suspend (Exception) -> Unit

open class BaseViewModel : ViewModel() {

    val loginStatusInvalid: MutableLiveData<Boolean> = MutableLiveData()
    val showLoadding = MutableLiveData(false)
    protected fun launch(
        block: Block<Unit>,
        error: Error? = null,
        cancel: Cancel? = null,
        isShowLoadding: Boolean = true,
        showErrorToast: Boolean = true
    ): Job {
        return viewModelScope.launch {
            try {
                if (isShowLoadding) {
                    showLoadding.value = true
                }
                block.invoke(this)
                delay(1000)
                showLoadding.value = false
            } catch (e: Exception) {
                delay(1000)
                showLoadding.value = false
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e, showErrorToast)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    private fun onError(e: Exception, showErrorToast: Boolean) {
        when (e) {
            is ApiException -> {
                when (e.code) {
                    40000 -> {
                        SPUtils.getInstance().remove("token")
                        loginStatusInvalid.value = true
                    }
                    else -> if (showErrorToast) showToast(e.message)
                }
            }
            is ConnectException, is SocketTimeoutException, is UnknownHostException, is HttpException, is SSLHandshakeException ->
                if (showErrorToast) showToast("请检查网络")
            is JSONException ->
                if (showErrorToast) showToast("data error")
            else ->
                if (showErrorToast) showToast("unknown error")
        }
    }

    protected fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async { block.invoke(this) }
    }
}