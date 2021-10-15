package com.mcgrady.pay.sdk.wechat

import android.content.Context
import com.mcgrady.pay.core.BasePayCall
import com.mcgrady.pay.core.PayChannel
import com.mcgrady.pay.core.PayClient
import com.mcgrady.pay.core.PayResult
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by mcgrady on 2021/10/13.
 */
class WechatPayCall constructor(param: PayReq) : BasePayCall<PayReq>(PayChannel.WECHAT_PAY, param) {

    companion object {
        var wxApi: IWXAPI? = null
            private set

        fun init(context: Context, appId: String) {
            wxApi = WXAPIFactory.createWXAPI(context.applicationContext, null).apply {
                registerApp(appId)
            }
        }
    }

    override fun send() {
        super.send()

        if (wxApi == null) {
            //throw IllegalArgumentException("WXAPI is not initialized!")
            PayClient.instance.postValue(PayResult.failed(channel, "WXAPI is not initialized!"))
        }

        wxApi?.let {
            if (!it.isWXAppInstalled()) {
                PayClient.instance.postValue(PayResult.failed(channel, "WeChat App is not installed!"))
                return@send
            }

            if (it.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
                PayClient.instance.postValue(
                    PayResult.failed(
                        channel,
                        "The current WeChat version does not support payment!"
                    )
                )
                return@send
            }

            it.sendReq(param)
        }
    }
}
