package com.mcgrady.pay.core

import androidx.annotation.CallSuper
import androidx.lifecycle.Observer

/**
 * Created by mcgrady on 2021/10/13.
 */
interface BasePayObserver : Observer<PayResult> {

    fun onSuccess()

    fun onFailed(message: String)

    fun onCancel()

    fun onComplete() {}

    @CallSuper
    override fun onChanged(result: PayResult?) {
        result?.let {
            when (result.status) {
                PayStatus.SUCCESS -> {
                    onSuccess()
                    onComplete()
                }
                PayStatus.FAILED -> {
                    onFailed(result.message ?: "未知错误")
                    onComplete()
                }
                PayStatus.CANCEL -> onCancel()
            }
        }
    }
}