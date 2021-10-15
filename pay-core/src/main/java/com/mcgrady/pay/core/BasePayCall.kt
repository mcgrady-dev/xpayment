package com.mcgrady.pay.core

import android.util.Log

/**
 * Created by mcgrady on 2021/10/13.
 */
abstract class BasePayCall<T> constructor(val channel: PayChannel, val param: T) {
    open fun send() {
        if (param == null) {
            Log.e(BasePayCall::class.simpleName, "Payment parameters cannot be empty!")
            return
        }
    }
}