package com.mcgrady.pay.core

/**
 * Created by mcgrady on 2021/10/13.
 */
data class PayResult(val channel: PayChannel, val status: PayStatus, val message: String? = null) {

    companion object {
        fun success(channel: PayChannel): PayResult = PayResult(channel, PayStatus.SUCCESS)

        fun failed(channel: PayChannel, msg: String): PayResult = PayResult(channel, PayStatus.FAILED, msg)

        fun cancel(channel: PayChannel): PayResult = PayResult(channel, PayStatus.CANCEL)
    }
}


enum class PayChannel {
    WECHAT_PAY,
    ALIPAY,
//    WECHAT_MINI_PROGRAM,
//    UNION_PAY
}

enum class PayStatus {
    SUCCESS,
    FAILED,
    CANCEL
}