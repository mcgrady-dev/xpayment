package com.mcgrady.xpay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.mcgrady.xpay.PayReqAble;
import com.mcgrady.xpay.interf.PayResultCallBack;
import com.mcgrady.xpay.util.AliUtils;

import java.util.Map;

import io.reactivex.Observable;


/**
 * <p>支付宝支付</p>
 *
 * @author: mcgrady
 * @date: 2019/1/9
 */

public class AliPayReq extends PayReqAble {

    private PayTask payTask;
    private String params;
    private AliPayParam paramsModel;

    /**
     * 获取权限使用的 RequestCode
     */
    private static final int PERMISSIONS_REQUEST_CODE = 1002;

    private AliPayReq(Builder builder) {
        payResultCallBack = builder.payResultCallBack;
        payTask = new PayTask(builder.activity);
        params = builder.params;
        paramsModel = builder.paramsModel;
    }




    /**
     * 支付宝支付业务示例
     */
    @Override
    public void pay() {
        if (paramsModel == null) {
            doPay(params);
        } else {
            if (TextUtils.isEmpty(paramsModel.appId) || (TextUtils.isEmpty(paramsModel.rsa2_private)
                    && TextUtils.isEmpty(paramsModel.rsa_private))) {
                Log.i("AliPayReq", "错误: 需要配置 APPID | RSA_PRIVATE");
                if (payResultCallBack != null) {
                    payResultCallBack.onPayFailure("错误: 需要配置 APPID | RSA_PRIVATE");
                }
                return;
            }

            boolean rsa2 = (paramsModel.rsa2_private.length() > 0);
            //Map<String, String> params = OrderInfoUtil.buildOrderParamMap(appId, rsa2);
            Map<String, String> params = AliUtils.buildOrderParamMap(paramsModel.appId, rsa2,
                    paramsModel.biz_content, paramsModel.charset, paramsModel.method,
                    paramsModel.timestamp, paramsModel.version);

            String orderParam = AliUtils.buildOrderInfo(params);
            String privateKey = rsa2 ? paramsModel.rsa2_private : paramsModel.rsa_private;
            String sign = AliUtils.getSign(params, privateKey, rsa2);
            String orderInfo = orderParam + "&" + sign;

            doPay(orderInfo);
        }
    }

    @SuppressLint("CheckResult")
    public void doPay(String orderParams) {
        if (TextUtils.isEmpty(orderParams)) {
            if (payResultCallBack != null) {
                payResultCallBack.onPayFailure("");
            }
            return;
        }

        Observable.fromCallable(() -> {
            return payTask.payV2(orderParams, true);
        }).map(stringMap -> {
            return new PayResult(stringMap);
        }).subscribe(payResult -> {

            /**
             * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                if (payResultCallBack != null) {
                    payResultCallBack.onPaySuccess(payResult.getResult());
                }
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                if (payResultCallBack != null) {
                    payResultCallBack.onPayFailure(payResult.getResult());
                }
            }
        });
    }

    public static final class Builder {
        private PayResultCallBack payResultCallBack;
        private Activity activity;
        private String params;
        private AliPayParam paramsModel;

        public Builder() {
        }

        public Builder payResultCallBack(PayResultCallBack val) {
            payResultCallBack = val;
            return this;
        }

        public Builder with(Activity val) {
            activity = val;
            return this;
        }

        public Builder params(String val) {
            params = val;
            return this;
        }

        public Builder paramsModel(AliPayParam val) {
            paramsModel = val;
            return this;
        }

        public AliPayReq create() {
            return new AliPayReq(this);
        }
    }


}
