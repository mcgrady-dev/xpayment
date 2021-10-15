package com.mcgrady.paysdk

import android.app.Application
import com.mcgrady.pay.sdk.wechat.WechatPayCall

/**
 * Created by mcgrady on 2021/10/13.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        WechatPayCall.init(this, "wx7e16cf49c52635e2")
    }
}