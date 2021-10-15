package com.mcgrady.paysdk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mcgrady.pay.core.BasePayObserver
import com.mcgrady.pay.core.PayClient
import com.mcgrady.pay.sdk.wechat.WechatPayCall
import com.mcgrady.paysdk.databinding.ActivityMainBinding
import com.tencent.mm.opensdk.modelpay.PayReq

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PayClient.instance.send(this, WechatPayCall(PayReq().apply {
            appId = "wx7e16cf49c52635e2"
            partnerId = "1900000109"
            prepayId = "1101000000140415649af9fc314aa427"
            packageValue = "Sign=WXPay"
            nonceStr = "1101000000140429eb40476f8896f4c9"
            timeStamp = "1398746574"
            sign = "7FFECB600D7157C5AA49810D2D8F28BC2811827B"
        }), object: BasePayObserver {
            override fun onSuccess() {

            }

            override fun onFailed(message: String) {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        PayClient.instance.removeObservers(this)
    }
}