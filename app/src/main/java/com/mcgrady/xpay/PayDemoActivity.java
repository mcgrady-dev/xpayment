package com.mcgrady.xpay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mcgrady.xpay.alipay.AliPayReq;
import com.mcgrady.xpay.interf.PayResultCallBack;
import com.mcgrady.xpay.wxpay.WechatPayReq;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class PayDemoActivity extends AppCompatActivity {

    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_demo);
        requestPermission(() -> {
            AliPayReq aliPayReq = new AliPayReq.Builder()
                    .with(new PayTask(PayDemoActivity.this))
                    .payResultCallBack(new PayResultCallBack() {
                        @Override
                        public void onPaySuccess(String result) {

                        }

                        @Override
                        public void onPayFailure(String result) {

                        }

                        @Override
                        public void onPayConfirm(String result) {

                        }

                        @Override
                        public void onPayCheck(String status) {

                        }
                    })
                    .create();

            PayAPI.getInstance().pay(aliPayReq);
        });


        WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                .with(WXAPIFactory.createWXAPI(PayDemoActivity.this, null))
                .appId("")
                .partnerId("")
                .prepayId("")
                .nonceStr("")
                .timeStamp("")
                .sign("")
                .payCallback(new PayResultCallBack() {
                    @Override
                    public void onPaySuccess(String result) {

                    }

                    @Override
                    public void onPayFailure(String result) {

                    }

                    @Override
                    public void onPayConfirm(String result) {

                    }

                    @Override
                    public void onPayCheck(String status) {

                    }
                })
                .create();

        PayAPI.getInstance().pay(wechatPayReq);
    }

    /**
     * 检查支付 SDK 所需的权限，并在必要的时候动态获取。
     * 在 targetSDK = 23 以上，READ_PHONE_STATE 和 WRITE_EXTERNAL_STORAGE 权限需要应用在运行时获取。
     * 如果接入支付宝 SDK 的应用 targetSdk 在 23 以下，可以省略这个步骤。
     */
    @SuppressLint("CheckResult")
    private void requestPermission(@NonNull CallBack callBack) {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }

        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 用户允许权限
                        callBack.callback();
                    } else {
                        Toast.makeText(PayDemoActivity.this, "无法获取支付宝 SDK 所需的权限, 请到系统设置开启", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private interface CallBack {
        void callback();
    }
}
