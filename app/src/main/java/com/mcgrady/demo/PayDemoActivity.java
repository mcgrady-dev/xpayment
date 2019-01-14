package com.mcgrady.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mcgrady.xpay.PayAPI;
import com.mcgrady.xpay.alipay.AliPayReq;
import com.mcgrady.xpay.interf.PayResultCallBack;
import com.mcgrady.xpay.wxpay.WeChatPayReq;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class PayDemoActivity extends AppCompatActivity {

    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_demo);

        testPay();
    }

    private void testPay() {
        requestPermission(() -> {
            AliPayReq aliPayReq = new AliPayReq.Builder()
                    .with(PayDemoActivity.this)
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


        WeChatPayReq weChatPayReq = new WeChatPayReq.Builder()
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

        PayAPI.getInstance().pay(weChatPayReq);
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

    /**
     * 将 H5 网页版支付转换成支付宝 App 支付的示例
     */
    public static void toAliH5Pay(Activity activity) {
        Intent intent = new Intent(activity, H5PayDemoActivity.class);
        Bundle extras = new Bundle();

        /**
         * URL 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
         *
         * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
         * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
         * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
         * 进行测试。
         *
         * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
         * 可以参考它实现自定义的 URL 拦截逻辑。
         */
        String url = "https://m.taobao.com";
        extras.putString("url", url);
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    private interface CallBack {
        void callback();
    }
}
