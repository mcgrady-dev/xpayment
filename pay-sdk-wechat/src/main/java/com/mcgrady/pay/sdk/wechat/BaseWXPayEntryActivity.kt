package com.mcgrady.pay.sdk.wechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.mcgrady.pay.core.PayChannel
import com.mcgrady.pay.core.PayClient
import com.mcgrady.pay.core.PayResult
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * Created by mcgrady on 2021/10/14.
 */
abstract class BaseWXPayEntryActivity : Activity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WechatPayCall.wxApi?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        WechatPayCall.wxApi?.handleIntent(intent, this)
    }

    override fun onResp(resp: BaseResp?) {
        resp?.let {
            if (it.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
                when (it.errCode) {
                    0 -> {
                        PayClient.instance.postValue(PayResult.success(PayChannel.WECHAT_PAY))
                    }
                    -1 -> {
                        PayClient.instance.postValue(PayResult.failed(PayChannel.WECHAT_PAY, it.errStr ?: "支付发生错误"))
                    }
                    -2 -> {
                        PayClient.instance.postValue(PayResult.cancel(PayChannel.WECHAT_PAY))
                    }
                }
            }
        }
        finish()
    }

    override fun onReq(req: BaseReq?) {
    }
}